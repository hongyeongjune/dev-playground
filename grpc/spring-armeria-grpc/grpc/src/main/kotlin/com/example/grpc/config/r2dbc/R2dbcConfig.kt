package com.example.grpc.config.r2dbc

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import java.time.LocalDateTime
import java.util.*

@Configuration
@EnableR2dbcRepositories
@EnableR2dbcAuditing
class R2dbcConfig {
    @Bean
    fun auditorProvider(): AuditorAware<LocalDateTime> {
        return AuditorAware { Optional.of(LocalDateTime.now()) }
    }
}