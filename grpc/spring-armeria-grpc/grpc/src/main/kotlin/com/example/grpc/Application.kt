package com.example.grpc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import reactor.blockhound.BlockHound
import reactor.blockhound.integration.BlockHoundIntegration
import java.io.FilterInputStream

@SpringBootApplication
@ConfigurationPropertiesScan
class Application

fun main(args: Array<String>) {
    BlockHound.install(
        BlockHoundIntegration {
            it
                .allowBlockingCallsInside(FilterInputStream::class.qualifiedName, "read")
        }
    )
    runApplication<Application>(*args)
}
