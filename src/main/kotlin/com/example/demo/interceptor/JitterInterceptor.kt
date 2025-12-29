package com.example.demo.interceptor

import io.grpc.*

class JitterInterceptor(private val strategy: JitterStrategy) : ClientInterceptor {

    override fun <ReqT, RespT> interceptCall(
        method: MethodDescriptor<ReqT, RespT>,
        callOptions: CallOptions,
        next: Channel
    ): ClientCall<ReqT, RespT> {

        val delegate = next.newCall(method, callOptions)

        return object : ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(delegate) {

            override fun sendMessage(message: ReqT) {
                val delay = strategy.getDelay()

                if (delay > 0) {
                    println(" Jitter Strategy applied: Waiting ${delay}ms")
                    try {
                        Thread.sleep(delay)
                    } catch (e: InterruptedException) {
                        Thread.currentThread().interrupt()
                    }
                }
                super.sendMessage(message)
            }
        }
    }
}