import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    // id("org.web3j") version "4.9.0"
    kotlin("plugin.serialization") version "1.6.10"
    application
    `maven-publish`
    id("com.expediagroup.graphql") version "6.4.0"

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
    implementation ("org.web3j:core:5.0.0")

    // JSON
    implementation("com.beust:klaxon:5.6")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.3.2")

    // Logging
    implementation("org.slf4j:slf4j-api:2.0.0-alpha7")
    implementation("org.slf4j:slf4j-simple:2.0.0-alpha7")
    implementation("io.github.microutils:kotlin-logging:2.1.21")

    // Config: Hoplite
    implementation("com.sksamuel.hoplite:hoplite-core:2.6.5")
    implementation("com.sksamuel.hoplite:hoplite-yaml:2.6.5")
    implementation("com.sksamuel.hoplite:hoplite-hikaricp:2.6.5")

    // HTTP / Server: Javalin
    implementation("io.javalin:javalin-bundle:4.4.0")

    // HTTP / Client: ktor
    implementation("io.ktor:ktor-client-core:2.1.3")
    implementation("io.ktor:ktor-client-content-negotiation:2.1.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.1.3")
    implementation("io.ktor:ktor-client-cio:2.1.3")
    implementation("io.ktor:ktor-client-logging:2.1.3")
    implementation("io.ktor:ktor-client-auth:2.1.3")

    // Persistence
    implementation("org.jetbrains.exposed:exposed-core:0.38.2")
    implementation("org.jetbrains.exposed:exposed-dao:0.38.2")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.38.2")
    implementation("org.xerial:sqlite-jdbc:3.36.0.3")

    // Testing
    //testImplementation(kotlin("test-junit"))
    //testImplementation("io.mockk:mockk:1.12.4")

    testImplementation("io.kotest:kotest-runner-junit5:5.3.1")
    testImplementation("io.kotest:kotest-assertions-core:5.3.1")
    testImplementation("io.kotest:kotest-assertions-json:5.3.1")

    //near
    implementation ("com.syntifi.near:near-java-api:0.1.0")

    // unique
    implementation ("network.unique:unique-sdk-jvm:0.0.1")

    // algorand
    implementation ("com.algorand:algosdk:2.2.0")

    // expediagroup graphql
    implementation("com.expediagroup:graphql-kotlin-spring-client:6.4.0")
    implementation("com.expediagroup", "graphql-kotlin-client-serialization", "6.4.0")

    implementation("com.expediagroup", "graphql-kotlin-spring-client","6.4.0") {
    exclude("com.expediagroup", "graphql-kotlin-client-jackson")
}
    implementation("com.expediagroup", "graphql-kotlin-client-serialization","6.4.0")


}

tasks.withType<Test> {
    useJUnitPlatform()

    // Use the following condition to optionally run the integration tests:
    // > gradle build -PrunIntegrationTests
    if (!project.hasProperty("runIntegrationTests")) {
        exclude("id/walt/nftkit/blockchainTransactionsCalls/**")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
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

graphql {
    client {
        endpoint = "https://scan-api.opal.uniquenetwork.dev/v1/graphql/"
        packageName = "id.walt.nftkit"
        customScalars = listOf(
            com.expediagroup.graphql.plugin.gradle.config.GraphQLScalar(
                "_Any",
                "kotlinx.serialization.json.JsonObject",
                "com.expediagroup.graphql.examples.client.gradle.AnyScalarConverter"
            ),
        )
        serializer = com.expediagroup.graphql.plugin.gradle.config.GraphQLSerializer.KOTLINX
    }
}

