package com.example.demo.io

import com.example.demo.grpc.ChatMessage

class ChatMessageFormatter : MessageFormatter {
    override fun format(message: ChatMessage, currentUsername: String): String {
        return if (message.user == currentUsername) {
            "You: ${message.message}"
        } else {
            "${message.user}: ${message.message}"
        }
    }
}
