package com.example.demo.client

import com.example.demo.grpc.ChatMessage
import com.example.demo.io.ConsoleIO
import com.example.demo.io.MessageFormatter
import kotlinx.coroutines.flow.Flow

class ClientMessageHandler(
    private val consoleIO: ConsoleIO,
    private val messageFormatter: MessageFormatter
) {
    suspend fun handleIncomingMessages(messageStream: Flow<ChatMessage>, username: String) {
        messageStream.collect { message ->
            val formattedMessage = messageFormatter.format(message, username)
            consoleIO.writeLine(formattedMessage)
        }
    }
}
