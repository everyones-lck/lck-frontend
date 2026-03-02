import every.lol.com.setNamespace

plugins {
    alias(libs.plugins.everylol.library)
    alias(libs.plugins.everylol.compose)
}

setNamespace("core.ui")

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.designsystem)
            implementation(projects.core.model)
            implementation(projects.core.common)

        }
    }
}