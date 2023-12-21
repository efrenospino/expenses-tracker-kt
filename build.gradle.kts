// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.kover) apply false
    alias(libs.plugins.sqldelight) apply false
}
true // Needed to make the Suppress annotation work for the plugins block
