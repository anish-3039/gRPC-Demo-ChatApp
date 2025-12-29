package com.example.demo.lifecycle

interface ClientLifecycleManager {
    fun shutdown()
    fun awaitTermination()
}
