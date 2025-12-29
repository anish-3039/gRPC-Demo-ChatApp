package com.example.demo.handler

import com.example.demo.grpc.ChatMessage
import com.example.demo.io.ConsoleIO
import com.example.demo.repository.MessageRepository

class ServerInputHandler(
    private val consoleIO: ConsoleIO,
    private val messageRepository: MessageRepository
) : InputHandler {

    override suspend fun handleInput(username: String) {
        while (true) {
            val rawMessage = consoleIO.readLine()
            processRawInput(rawMessage, username)?.let { chatMessage ->
                messageRepository.broadcast(chatMessage)
            }
        }
    }

    override suspend fun processRawInput(rawMessage: String, username: String): ChatMessage? {
        return if (rawMessage.isNotEmpty()) {
            ChatMessage.newBuilder()
                .setUser(username)
                .setMessage(rawMessage)
                .build()
        } else {
            null
        }
    }
}
