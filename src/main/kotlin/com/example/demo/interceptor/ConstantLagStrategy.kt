package com.example.demo.interceptor

class ConstantLagStrategy(
    private val delayMs : Long = 500
) : JitterStrategy{
    override fun getDelay() : Long = delayMs
}