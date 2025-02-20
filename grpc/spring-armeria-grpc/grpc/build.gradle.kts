import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.protobuf

val armeriaVersion = "1.27.0"
val grpcKotlinVersion = "1.4.1"
val grpcProtoVersion = "1.63.0"
val grpcVersion = "3.25.3"

group = "com.example.grpc"

plugins {
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    id("com.google.protobuf") version "0.9.4"
}

repositories {
    mavenCentral()
}

dependencies {
    // https://github.com/grpc/grpc-kotlin/blob/master/examples/stub/build.gradle.kts
    protobuf(project(":proto"))

    // m1 os
    implementation("io.netty:netty-resolver-dns-native-macos:4.1.76.Final:osx-aarch_64")

    // block hound
    implementation("io.projectreactor.tools:blockhound:1.0.9.RELEASE")

    // logging
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

    // kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // armeria
    implementation(platform("io.netty:netty-bom:4.1.106.Final"))
    implementation(platform("com.linecorp.armeria:armeria-bom:$armeriaVersion"))
    implementation("com.linecorp.armeria:armeria-kotlin:$armeriaVersion")
    implementation("com.linecorp.armeria:armeria-spring-boot3-starter:$armeriaVersion")

    // grpc
    implementation("com.linecorp.armeria:armeria-grpc:$armeriaVersion")
    implementation("io.grpc:grpc-stub:$grpcProtoVersion")
    implementation("io.grpc:grpc-protobuf:$grpcProtoVersion")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion") // kotlin stub 제공
    implementation("com.google.protobuf:protobuf-kotlin:$grpcVersion") // kotlin 코드 생성 도구

    // r2dbc
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("io.asyncer:r2dbc-mysql:1.1.0")
}

protobuf {
// Configure the protoc executable.
    protoc {
        // Download from the repository.
        artifact = "com.google.protobuf:protoc:$grpcVersion"
    }

    // Locate the codegen plugins.
    plugins {
        // Locate a plugin with name 'grpc'.
        id("grpc") {
            // Download from the repository.
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcProtoVersion"
        }
        // Locate a plugin with name 'grpcKt'.
        id("grpckt") {
            // Download from the repository.
            artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion:jdk8@jar"
        }
    }

    // generate code
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
            it.builtins {
                id("kotlin")
            }
        }
    }
}