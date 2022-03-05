import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.web3j") version "4.9.0"
    kotlin("plugin.serialization") version "1.6.10"
    application
    `maven-publish`
}

group = "id.walt"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.walt.id/repository/waltid/")
    maven("https://maven.walt.id/repository/waltid-ssi-kit/")
    mavenLocal()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation ("org.web3j:core:4.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.3.2")
    implementation("io.javalin:javalin-bundle:4.3.0")
    implementation("com.beust:klaxon:5.5")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
}

application {
    mainClass.set("id.walt.nftkit.AppKt")
}

solidity {
    optimizeRuns = 500
}

web3j {
    generatedPackageName = "com.mycompany.{0}"
    generatedFilesBaseDir = "$buildDir/custom/destination"
    useNativeJavaTypes = false
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            pom {
                name.set("walt.id NFT Kit")
                description.set("Kotlin/Java for minting & managing NFTs.")
                url.set("https://walt.id")
            }
            from(components["java"])
        }
    }

    repositories {
        maven {
            url = uri("https://maven.walt.id/repository/waltid-nft-kit/")
            val usernameFile = File("secret_maven_username.txt")
            val passwordFile = File("secret_maven_password.txt")
            val secretMavenUsername = System.getenv()["MAVEN_USERNAME"] ?: if (usernameFile.isFile) { usernameFile.readLines()[0] } else { "" }
            val secretMavenPassword = System.getenv()["MAVEN_PASSWORD"] ?: if (passwordFile.isFile) { passwordFile.readLines()[0] } else { "" }

            credentials {
                username = secretMavenUsername
                password = secretMavenPassword
            }
        }
    }
}