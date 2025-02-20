package com.example.grpc.config.armeria

import com.example.grpc.Application
import com.example.grpc.interceptor.GlobalExceptionInterceptor
import com.example.grpc.interceptor.SimpleLoggingInterceptor
import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.server.grpc.GrpcService
import com.linecorp.armeria.spring.ArmeriaServerConfigurator
import io.asyncer.r2dbc.mysql.client.Client
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import io.grpc.kotlin.AbstractCoroutineServerImpl
import io.netty.handler.ssl.ClientAuth
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

@Configuration
class ArmeriaConfig {
    @Bean
    fun grpcService(allServiceBean: List<AbstractCoroutineServerImpl>): GrpcService {
        val grpcServiceBuilder = GrpcService.builder()
            .enableUnframedRequests(true)
            .intercept(SimpleLoggingInterceptor(), GlobalExceptionInterceptor())

        allServiceBean.forEach {
            Client.logger.info("Register Grpc Bean : {}", it.javaClass.name)
            grpcServiceBuilder.addService(it)
        }

        return grpcServiceBuilder.build()
    }

    @Bean
    fun armeriaServerConfigurator(grpcService: GrpcService): ArmeriaServerConfigurator {
        return ArmeriaServerConfigurator {
            /**
             * Max Request Length 증설
             */
            it.maxRequestLength(32 * 1024 * 1024)

            /**
             * Grpc 사용을 위한 서비스 등록
             */
            it.service(grpcService)

            /**
             * Docs 생성을 위한 서비스 등록
             */
            it.serviceUnder("/docs", DocService())

            /**
             * mTLS 적용
             */
            val serverCertInputStream = Application::class.java.classLoader.getResourceAsStream("tls/server.crt")!!
            val serverKeyInputStream = Application::class.java.classLoader.getResourceAsStream("tls/server.key")!!
            val caCertInputStream = Application::class.java.classLoader.getResourceAsStream("tls/ca.crt")!!

            it.https(8443)
            serverCertInputStream.use { serverCertStream ->
                serverKeyInputStream.use { serverKeyStream ->
                    it.tls(serverCertStream, serverKeyStream)
                }
            }
            it.tlsCustomizer { builder ->
                caCertInputStream.use { caCertStream ->
                    val caCert = CertificateFactory.getInstance("X.509").generateCertificate(caCertStream) as X509Certificate
                    builder.trustManager(caCert)
                }
                builder.clientAuth(ClientAuth.REQUIRE)
            }
        }
    }
}