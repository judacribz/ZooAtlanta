@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.kotlinKapt) apply false
    alias(libs.plugins.anvil) apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
