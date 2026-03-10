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
                level = LogLevel.BODY
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

                    if (statusCode in 300..599) {
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
        // 인증 관련
        1000L, 2001L -> DomainException.InvalidJwtTokenException(cause = cause)
        1001L -> DomainException.UnsupportedSocialProviderException(cause = cause)

        // 유저 관련
        2000L -> DomainException.AlreadyRegisteredUserException(cause = cause)
        3000L -> DomainException.UserNotFoundException(cause = cause)

        // 권한 및 공통 에러
        5002L -> DomainException.NoPermissionException(cause = cause)
        9000L -> DomainException.ServerErrorException(cause = cause)
        9001L -> DomainException.InvalidInputException(cause = cause)

        // 기본 에러
        else -> DomainException.UnknownException(cause = cause)
    }
}