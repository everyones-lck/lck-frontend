import every.lol.com.configureKotlin
import every.lol.com.configureCoroutineKmp
import every.lol.com.configureKotlinMultiplatform

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
}

configureKotlinMultiplatform()
configureKotlin()
configureCoroutineKmp()