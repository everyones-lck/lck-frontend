import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import every.lol.com.setNamespace

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.everylol.library)
    alias(libs.plugins.buildconfig)
}

setNamespace("core.network")

fun getProperty(key: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(key)
        ?: error("\n\n[에러] local.properties 파일에 '$key' 설정이 누락되었습니다! \nBASE_URL=https://api.example.com/ 형태의 주소를 추가해주세요.\n")
}

buildConfig {
    packageName("every.lol.com.core.network")

    val isDebug = project.gradle.startParameter.taskNames.any { it.contains("debug", ignoreCase = true) }
    buildConfigField("boolean", "DEBUG", isDebug.toString())

    buildConfigField("String", "BASE_URL", "\"${getProperty("BASE_URL")}\"")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(projects.core.domain)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.engine)
        }
    }
}