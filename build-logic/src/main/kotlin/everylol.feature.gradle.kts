import com.android.build.api.dsl.LibraryExtension
import every.lol.com.libs
import gradle.kotlin.dsl.accessors._55a9afa7f6f82745ecdb5056ac330f56.implementation

plugins {
    id("everylol.library")
    id("everylol.compose")
}

extensions.configure<LibraryExtension> {
    packaging {
        resources.excludes.add("META-INF/**")
    }
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}


dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:datastore"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:navigation"))
    implementation(project(":core:network"))
    implementation(project(":core:ui"))

    implementation(libs.findLibrary("androidx-navigation-compose").get())

/*    implementation(libs.findLibrary("hilt.navigation.compose").get())
    implementation(libs.findLibrary("androidx.compose.navigation").get())
    androidTestImplementation(libs.findLibrary("androidx.compose.navigation.test").get())*/
}