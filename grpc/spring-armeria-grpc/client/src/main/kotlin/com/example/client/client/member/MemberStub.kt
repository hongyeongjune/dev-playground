package com.example.client.client.member

import com.example.client.config.StubFactory
import com.example.proto.member.MemberHandlerGrpcKt
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class MemberStub(
    private val stubFactory: StubFactory,
) {
    @Bean
    fun memberServiceStub(): MemberHandlerGrpcKt.MemberHandlerCoroutineStub {
        return stubFactory.createStub(MemberHandlerGrpcKt.MemberHandlerCoroutineStub::class)
    }
}