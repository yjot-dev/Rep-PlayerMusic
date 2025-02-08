// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript{
    extra.apply {
        set("roomVersion","2.6.1")
        set("navigationVersion","2.8.5")
        set("coilVersion","2.7.0")
    }
}

plugins {
    id("com.android.application") version "8.5.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.20" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false
}