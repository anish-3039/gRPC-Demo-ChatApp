package com.example.demo.interceptor

interface JitterStrategy {
    fun getDelay() : Long
}