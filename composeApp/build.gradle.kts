import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties

plugins {
    alias(libs.plugins.everylol.application)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.projectDir.resolve("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use(localProperties::load)
}

val kakaoAppKey = (
            localProperties.getProperty("KAKAO_APP_KEY")
                        ?: providers.gradleProperty("KAKAO_APP_KEY").orNull
                        ?: System.getenv("KAKAO_APP_KEY")
                ).orEmpty()
    .trim()
    .takeIf { it.isNotEmpty() }
    ?: error("KAKAO_APP_KEY를 local.properties, Gradle property, 또는 환경변수로 설정해주세요.")

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            export(project(":core:network"))
        }
    }
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.koin.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.kakao.sdk.user)
        }

        commonMain.dependencies {

            implementation(projects.core.common)
            implementation(projects.core.data)
            implementation(projects.core.datastore)
            implementation(projects.core.designsystem)
            implementation(projects.core.domain)
            implementation(projects.core.model)
            implementation(projects.core.navigation)
            implementation(projects.core.network)
            implementation(projects.core.ui)

            implementation(projects.feature.aboutlck)
            implementation(projects.feature.community)
            implementation(projects.feature.home)
            implementation(projects.feature.intro)
            implementation(projects.feature.main)
            implementation(projects.feature.matches)
            implementation(projects.feature.mypage)

            implementation(libs.precompose)
            implementation(libs.precompose.viewmodel)

            implementation(libs.koin.compose)
            implementation(libs.koin.core)
            implementation(libs.ktor.client.core)

            api(project(":core:network"))
        }

        commonTest.dependencies {

        }

        iosMain.dependencies {

        }
    }
}

android {
    namespace = "every.lol.com"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "every.lol.com"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        manifestPlaceholders["KAKAO_APP_KEY"] = kakaoAppKey

        buildConfigField("String", "KAKAO_APP_KEY", "\"$kakaoAppKey\"")

    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        buildConfig = true
    }
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    debugImplementation(libs.compose.ui.tooling)
}

