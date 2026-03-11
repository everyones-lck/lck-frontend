package every.lol.com.core.network.di

import every.lol.com.core.network.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
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


            expectSuccess = false

            defaultRequest {
                contentType(ContentType.Application.Json)
                url {
                    val baseUrl = BuildConfig.BASE_URL
                    val extractedProtocol = if (baseUrl.startsWith("https://")) URLProtocol.HTTPS else URLProtocol.HTTP
                    val extractedHost = baseUrl.removePrefix("https://").removePrefix("http://").removeSuffix("/")

                    protocol = extractedProtocol
                    host = extractedHost
                }
                header(HttpHeaders.ContentType, "application/json")
            }
        }
    }
}