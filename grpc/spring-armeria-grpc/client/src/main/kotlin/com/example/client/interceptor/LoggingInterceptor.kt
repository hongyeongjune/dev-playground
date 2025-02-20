package com.example.client.interceptor

import io.grpc.CallOptions
import io.grpc.Channel
import io.grpc.ClientCall
import io.grpc.ClientInterceptor
import io.grpc.MethodDescriptor
import mu.KotlinLogging

class LoggingInterceptor : ClientInterceptor {
    override fun <ReqT : Any?, RespT : Any?> interceptCall(
        method: MethodDescriptor<ReqT, RespT>,
        callOptions: CallOptions,
        next: Channel,
    ): ClientCall<ReqT, RespT> {

        logger.info { "before call grpc service" }

        return next.newCall(method, callOptions)
    }

    companion object {
        val logger = KotlinLogging.logger { }
    }
}
