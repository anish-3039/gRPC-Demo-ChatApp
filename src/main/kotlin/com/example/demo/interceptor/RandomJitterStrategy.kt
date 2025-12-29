package com.example.demo.interceptor
import kotlin.random.Random

class RandomJitterStrategy (
    private val minDelayMs : Long = 100,
    private val maxDelayMs : Long = 1000,
)  : JitterStrategy {
    override fun getDelay() : Long = Random.nextLong(minDelayMs, maxDelayMs)
}
