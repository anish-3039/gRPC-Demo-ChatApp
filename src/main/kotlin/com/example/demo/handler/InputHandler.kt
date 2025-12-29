package com.example.demo.handler

import com.example.demo.grpc.ChatMessage

interface InputHandler {
    suspend fun handleInput(username: String)
    suspend fun processRawInput(rawMessage: String, username: String): ChatMessage?
}
