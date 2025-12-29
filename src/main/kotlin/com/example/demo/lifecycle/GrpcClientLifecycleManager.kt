package com.example.demo.lifecycle

import io.grpc.ManagedChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import java.util.concurrent.TimeUnit

class GrpcClientLifecycleManager(
    private val channel: ManagedChannel,
    private val coroutineScope: CoroutineScope
) : ClientLifecycleManager {

    override fun shutdown() {
        coroutineScope.cancel()
        channel.shutdown()
    }

    override fun awaitTermination() {
        channel.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS)
    }
}
