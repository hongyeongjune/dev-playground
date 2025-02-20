package com.example.client.config

import com.example.client.interceptor.TimeoutInterceptor
import io.grpc.CallOptions
import io.grpc.ManagedChannel
import io.grpc.kotlin.AbstractCoroutineStub
import org.springframework.stereotype.Component
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

@Component
class StubFactory(
    private val grpcProperties: GrpcProperties,
    private val grpcChannel: ManagedChannel,
) {

    /**
     * withDeadlineAfter 를 적용하거나 callOption 으로 deadline 을 적용하고 빈으로 등록하는 경우 빈 등록 시점부터 카운트가 들어가므로 timeout 옵션은 interceptor 로 부여하거나 사용처마다 withDeadlineAfter 를 별도로 정의하고 사용해야 한다.
     */
    fun <T> createStub(
        stubClass: KClass<T>,
        timeout: Long = grpcProperties.timeout,
    ): T where T : AbstractCoroutineStub<T> {
        val constructor = stubClass.primaryConstructor!!
        return constructor.call(grpcChannel, CallOptions.DEFAULT)
            .withInterceptors(TimeoutInterceptor(timeout))
    }
}
