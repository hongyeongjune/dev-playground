package com.example.client.config

import com.example.client.Application
import com.example.client.interceptor.LoggingInterceptor
import io.grpc.ManagedChannel
import io.grpc.netty.GrpcSslContexts
import io.grpc.netty.NegotiationType
import io.grpc.netty.NettyChannelBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GrpcChannelConfig(
    private val grpcProperties: GrpcProperties,
) {
    @Bean
    fun grpcChannel(): ManagedChannel {
        val clientCertInputStream = Application::class.java.classLoader.getResourceAsStream("tls/client.crt")!!
        val clientKeyInputStream = Application::class.java.classLoader.getResourceAsStream("tls/client.key")!!

        val builder = NettyChannelBuilder.forAddress(grpcProperties.endpoint, grpcProperties.port)
            .intercept(LoggingInterceptor())
            .negotiationType(NegotiationType.TLS)

        clientCertInputStream.use { clientCertStream ->
            clientKeyInputStream.use { clientKeyStream ->
                builder
                    .sslContext(
                        GrpcSslContexts.forClient()
                            .trustManager(InsecureTrustManagerFactory.INSTANCE)
                            .keyManager(clientCertStream, clientKeyStream)
                            .build(),
                    )
                    .build()
            }
        }

        return builder.build()
    }
}