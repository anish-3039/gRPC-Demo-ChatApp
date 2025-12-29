package com.example.demo.handler

import com.example.demo.grpc.ChatMessage
import com.example.demo.io.ConsoleIO
import kotlinx.coroutines.channels.Channel

class ClientInputHandler(
    private val consoleIO: ConsoleIO,
    private val messageChannel: Channel<ChatMessage>
) : InputHandler {

    override suspend fun handleInput(username: String) {
        while (true) {
            val rawMessage = consoleIO.readLine()
            processRawInput(rawMessage, username)?.let { chatMessage ->
                messageChannel.send(chatMessage)
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
