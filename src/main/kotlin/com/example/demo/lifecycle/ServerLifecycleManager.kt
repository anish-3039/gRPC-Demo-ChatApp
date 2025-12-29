package com.example.demo.lifecycle

interface ServerLifecycleManager {
    fun start()
    fun stop()
    fun blockUntilShutdown()
    fun registerShutdownHook(onShutdown: () -> Unit)
}
