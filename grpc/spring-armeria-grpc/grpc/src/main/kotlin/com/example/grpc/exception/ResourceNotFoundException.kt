package com.example.grpc.exception

class ResourceNotFoundException(s: String) : RuntimeException(s) {
    companion object {
        fun of(resourceName: String): ResourceNotFoundException {
            return ResourceNotFoundException("$resourceName not found.")
        }
    }
}
