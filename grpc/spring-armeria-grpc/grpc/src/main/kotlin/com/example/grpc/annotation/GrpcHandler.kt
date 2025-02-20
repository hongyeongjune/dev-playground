package com.example.grpc.annotation

import org.springframework.stereotype.Component

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Component
annotation class GrpcHandler
