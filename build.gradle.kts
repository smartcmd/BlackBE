plugins {
    id("java-library")
    id("org.allaymc.gradle.plugin") version "0.2.1"
}

group = "me.daoge.blackbe"
description = "BlackBE plugin for allay server"
version = "0.1.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

allay {
    api = "0.18.0"

    plugin {
        entrance = ".BlackBE"
        authors += "DaoGe"
        website = "https://github.com/AllayMC/JavaPluginTemplate"
    }
}

dependencies {
    compileOnly(group = "org.projectlombok", name = "lombok", version = "1.18.34")
    implementation(group = "com.squareup.okhttp3", name = "okhttp", version = "5.3.2")
    implementation(group = "com.google.code.gson", name = "gson", version = "2.11.0")
    annotationProcessor(group = "org.projectlombok", name = "lombok", version = "1.18.34")
}
