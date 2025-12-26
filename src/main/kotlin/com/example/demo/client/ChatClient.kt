package com.example.demo.client

import com.example.demo.grpc.ChatServiceGrpcKt
import com.example.demo.grpc.ChatMessage
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Scanner
import kotlinx.coroutines.channels.Channel
import com.example.demo.config.ClientConfig

class ChatClient(private val config: ClientConfig) {
    private lateinit var clientName: String

    private val channel = ManagedChannelBuilder
        .forAddress(config.host, config.port)
        .usePlaintext().build()

    private val client = ChatServiceGrpcKt.ChatServiceCoroutineStub(channel)

    fun start() {
        println("Enter your name:")
        val scanner = Scanner(System.`in`)
        clientName = scanner.nextLine()

        val messageChannel = Channel<ChatMessage>(Channel.UNLIMITED)

        CoroutineScope(Dispatchers.IO).launch {
            client.chat(messageChannel.receiveAsFlow()).collect { message ->
                if (message.user != clientName) {
                    println("${message.user}: ${message.message}")
                }else{
                    println("You: ${message.message}")
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val message = scanner.nextLine()
                if (message.isNotEmpty()) {
                    val chatMessage = ChatMessage.newBuilder()
                        .setUser(clientName)
                        .setMessage(message)
                        .build()
                    messageChannel.send(chatMessage)
                }
            }
        }
    }
}

