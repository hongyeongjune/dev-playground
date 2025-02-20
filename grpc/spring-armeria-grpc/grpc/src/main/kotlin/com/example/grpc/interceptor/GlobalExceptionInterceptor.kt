package com.example.grpc.interceptor

import com.example.grpc.exception.ResourceNotFoundException
import io.grpc.ForwardingServerCall
import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import io.grpc.Status
import mu.KotlinLogging

class GlobalExceptionInterceptor : ServerInterceptor {
    override fun <ReqT : Any?, RespT : Any?> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        return next.startCall(ExceptionServerCall(call), headers)
    }

    class ExceptionServerCall<ReqT, RespT>(
        delegate: ServerCall<ReqT, RespT>,
    ) : ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(delegate) {

        override fun close(status: Status, trailers: Metadata?) {
            if (status.isOk) {
                super.close(status, trailers)
            } else {
                val exceptionStatus: Status = handleException(status.cause)
                logger.error("gRPC exception : \n$exceptionStatus", status.cause)
                super.close(exceptionStatus, trailers)
            }
        }

        private fun handleException(e: Throwable?): Status {
            when (e) {
                is ResourceNotFoundException -> return Status.NOT_FOUND.withDescription(e.message)
            }

            return Status.INTERNAL.withDescription(e?.message)
        }
    }

    companion object {
        val logger = KotlinLogging.logger { }
    }
}