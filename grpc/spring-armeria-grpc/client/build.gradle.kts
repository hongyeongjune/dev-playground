import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.protobuf

val grpcKotlinVersion = "1.4.1"
val grpcProtoVersion = "1.63.0"
val grpcVersion = "3.25.3"

group = "com.example.client"

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

    // logging
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

    // kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // spring
    implementation("org.springframework.boot:spring-boot-starter-web")

    // webflux
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // grpc
    implementation("io.grpc:grpc-stub:$grpcProtoVersion")
    implementation("io.grpc:grpc-protobuf:$grpcProtoVersion")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion") // kotlin stub 제공
    implementation("com.google.protobuf:protobuf-kotlin:$grpcVersion") // kotlin 코드 생성 도구
    implementation("io.grpc:grpc-netty:$grpcProtoVersion") // stub NettyChannel 에 사용
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