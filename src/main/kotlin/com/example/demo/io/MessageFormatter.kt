package com.example.demo.io

import com.example.demo.grpc.ChatMessage

interface MessageFormatter {
    fun format(message: ChatMessage, currentUsername: String): String
}
