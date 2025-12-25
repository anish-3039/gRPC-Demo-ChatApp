package com.example.demo.repository

import com.example.demo.grpc.ChatMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class MessageRepository {
    private val _messages = MutableSharedFlow<ChatMessage>(
        replay = 100,
        extraBufferCapacity = 50
    )
    val messages: SharedFlow<ChatMessage> = _messages.asSharedFlow()

    suspend fun broadcast(chatMessage: ChatMessage) {
        _messages.emit(chatMessage)
    }
}