import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.10"
    // id("org.web3j") version "4.9.0"
    kotlin("plugin.serialization") version "1.9.10"
    application
    `maven-publish`
    id("com.expediagroup.graphql") version "6.5.3"

    id("com.github.ben-manes.versions") version "0.48.0"
}

group = "id.walt"
version = "1.SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.walt.id/repository/waltid/")
    maven("https://maven.walt.id/repository/waltid-ssi-kit/")
    mavenLocal()
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib"))

    // Ethereum: Web3j
    implementation("org.web3j:core:4.10.3") // 5.0.0 is an invalid old version

    // JSON
    implementation("com.beust:klaxon:5.6")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.6.0")

    // Logging
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("org.slf4j:slf4j-simple:2.0.9")
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")

    // Config: Hoplite
    implementation("com.sksamuel.hoplite:hoplite-core:2.8.0.RC3")
    implementation("com.sksamuel.hoplite:hoplite-yaml:2.8.0.RC3")
    implementation("com.sksamuel.hoplite:hoplite-hikaricp:2.8.0.RC3")

    // HTTP / Server: Javalin
    implementation("io.javalin:javalin-bundle:4.6.8")

    // HTTP / Client: ktor
    implementation("io.ktor:ktor-client-core:2.3.4")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.4")
    implementation("io.ktor:ktor-client-cio:2.3.4")
    implementation("io.ktor:ktor-client-logging:2.3.4")
    implementation("io.ktor:ktor-client-auth:2.3.4")



    // Persistence
    /*implementation("org.jetbrains.exposed:exposed-core:0.43.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.43.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.43.0")
    implementation("org.xerial:sqlite-jdbc:3.42.0.1")*/

    // Testing
    //testImplementation(kotlin("test-junit"))
    //testImplementation("io.mockk:mockk:1.12.4")

    testImplementation("io.kotest:kotest-runner-junit5:5.7.2")
    testImplementation("io.kotest:kotest-assertions-core:5.7.2")
    testImplementation("io.kotest:kotest-assertions-json:5.7.2")

    //near
    implementation ("com.syntifi.near:near-java-api:0.1.0")

    // unique
    implementation ("network.unique:unique-sdk-jvm:0.0.3")

    // algorand
    implementation ("com.algorand:algosdk:2.2.0")

    // expediagroup graphql
    implementation("com.expediagroup", "graphql-kotlin-client-serialization", "6.5.3")

    implementation("com.expediagroup", "graphql-kotlin-spring-client","6.5.3") {
        exclude("com.expediagroup", "graphql-kotlin-client-jackson")
    }
    implementation("com.expediagroup", "graphql-kotlin-client-serialization","6.5.3")
}

tasks.withType<Test> {
    useJUnitPlatform()

    // Use the following condition to optionally run the integration tests:
    // > gradle build -PrunIntegrationTests
    if (!project.hasProperty("runIntegrationTests")) {
        exclude("id/walt/nftkit/blockchainTransactionsCalls/**")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

application {
    mainClass.set("id.walt.nftkit.AppKt")
}
/*
solidity {
    optimizeRuns = 500
}

web3j {
    generatedPackageName = "com.mycompany.{0}"
    generatedFilesBaseDir = "$buildDir/custom/destination"
    useNativeJavaTypes = false
}
*/
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
            url = uri("https://maven.walt.id/repository/waltid/")
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

//graphql {
//    client {
//        endpoint = "https://scan-api.opal.uniquenetwork.dev/v1/graphql/"
//        packageName = "id.walt.nftkit"
//        customScalars = listOf(
//            com.expediagroup.graphql.plugin.gradle.config.GraphQLScalar(
//                "_Any",
//                "kotlinx.serialization.json.JsonObject",
//                "com.expediagroup.graphql.examples.client.gradle.AnyScalarConverter"
//            ),
//        )
//        serializer = com.expediagroup.graphql.plugin.gradle.config.GraphQLSerializer.KOTLINX
//    }
//}
//
