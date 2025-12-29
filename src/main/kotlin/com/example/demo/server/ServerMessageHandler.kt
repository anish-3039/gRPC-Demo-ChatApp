package com.example.demo.server

import com.example.demo.io.ConsoleIO
import com.example.demo.io.MessageFormatter
import com.example.demo.repository.MessageRepository

class ServerMessageHandler(
    private val consoleIO: ConsoleIO,
    private val messageFormatter: MessageFormatter,
    private val messageRepository: MessageRepository
) {
    suspend fun handleIncomingMessages(username: String) {
        messageRepository.messages.collect { message ->
            val formattedMessage = messageFormatter.format(message, username)
            consoleIO.writeLine(formattedMessage)
        }
    }
}
