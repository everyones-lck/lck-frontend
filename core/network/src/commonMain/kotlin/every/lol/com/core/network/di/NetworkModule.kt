package every.lol.com.core.network.di

import every.lol.com.core.domain.DomainException
import every.lol.com.core.network.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {

    single {
        Json {
            isLenient = true
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    single {
        HttpClient(get<HttpClientEngineFactory<*>>()) {
            install(ContentNegotiation) {
                json(get())
            }

            install(Logging) {
                level = if (BuildConfig.DEBUG) LogLevel.BODY else LogLevel.INFO
                logger = object : Logger {
                    override fun log(message: String) {
                        println("Ktor Log: $message")
                    }
                }
            }


            defaultRequest {
                contentType(ContentType.Application.Json)
                url {
                    val baseUrl = BuildConfig.BASE_URL
                    val extractedProtocol: URLProtocol
                    val extractedHost: String

                    if (baseUrl.startsWith("https://")) {
                        extractedProtocol = URLProtocol.HTTPS
                        extractedHost = baseUrl.removePrefix("https://").removeSuffix("/")
                    } else if (baseUrl.startsWith("http://")) {
                        extractedProtocol = URLProtocol.HTTP
                        extractedHost = baseUrl.removePrefix("http://").removeSuffix("/")
                    } else {
                        extractedProtocol = URLProtocol.HTTPS
                        extractedHost = baseUrl.removeSuffix("/")
                    }
                    protocol = extractedProtocol
                    host = extractedHost
                }
                header(HttpHeaders.ContentType, "application/json")
            }

            expectSuccess = false

            install(HttpCallValidator) {
                validateResponse { response ->
                    val statusCode = response.status.value

                    if (statusCode in 400..599) {
                        val errorMessage = "HTTP Error $statusCode: ${response.status.description}"

                        throw mapErrorCodeToDomainException(
                            errorCode = statusCode.toLong(),
                            cause = Throwable(errorMessage)
                        )
                    }
                }

                handleResponseExceptionWithRequest { cause, _ ->
                    throw cause
                }
            }
        }
    }
}

fun mapErrorCodeToDomainException(errorCode: Long, cause: Throwable? = null): DomainException {
    return when (errorCode) {
        401L -> DomainException.InvalidJwtTokenException(cause = cause)
        403L -> DomainException.NoPermissionException(cause = cause)
        500L -> DomainException.ServerErrorException(cause = cause)

        1000L, 2001L -> DomainException.InvalidJwtTokenException(cause = cause)
        else -> DomainException.UnknownException(cause = cause)
    }
}