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

class ChatClient {
    private lateinit var clientName: String

    fun start() {
        println("Enter your name:")
        val scanner = Scanner(System.`in`)
        clientName = scanner.nextLine()

        val channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build()
        val client = ChatServiceGrpcKt.ChatServiceCoroutineStub(channel)

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

fun main() {
    val client = ChatClient()
    client.start()
    Thread.currentThread().join()
}
