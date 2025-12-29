package com.example.demo.lifecycle

import io.grpc.Server

class GrpcServerLifecycleManager(
    private val server: Server
) : ServerLifecycleManager {

    override fun start() {
        server.start()
    }

    override fun stop() {
        server.shutdown()
    }

    override fun blockUntilShutdown() {
        server.awaitTermination()
    }

    override fun registerShutdownHook(onShutdown: () -> Unit) {
        Runtime.getRuntime().addShutdownHook(Thread {
            println("*** shutting down gRPC server since JVM is shutting down")
            onShutdown()
            println("*** server shut down")
        })
    }
}
