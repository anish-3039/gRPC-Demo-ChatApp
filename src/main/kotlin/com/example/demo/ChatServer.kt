package com.example.demo

import com.example.demo.grpc.ChatServiceGrpcKt
import com.example.demo.grpc.ChatMessage
import io.grpc.ServerBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Scanner

class ChatServer {
    private val chatHistory = MutableSharedFlow<ChatMessage>()
    private lateinit var serverName: String

    private val server = ServerBuilder.forPort(50051)
        .addService(ChatServiceImpl(chatHistory))
        .build()

    fun start() {
        server.start()
        println("Enter your name:")
        val scanner = Scanner(System.`in`)
        serverName = scanner.nextLine()

        // Coroutine to handle server's input and send messages
        CoroutineScope(Dispatchers.IO).launch {
            listenForInput(scanner)
        }

        // Coroutine to listen to chat history and print to server console
        CoroutineScope(Dispatchers.Default).launch {
            chatHistory.collect { message ->
                if (message.user == serverName) {
                    println("You: ${message.message}")
                } else {
                    println("${message.user}: ${message.message}")
                }
            }
        }

        Runtime.getRuntime().addShutdownHook(Thread {
            println("*** shutting down gRPC server since JVM is shutting down")
            this@ChatServer.stop()
            println("*** server shut down")
        })
    }

    private fun stop() {
        server.shutdown()
    }

    fun blockUntilShutdown() {
        server.awaitTermination()
    }

    private suspend fun listenForInput(scanner: Scanner) {
        while (true) {
            val message = scanner.nextLine()
            if (message.isNotEmpty()) {
                val chatMessage = ChatMessage.newBuilder()
                    .setUser(serverName)
                    .setMessage(message)
                    .build()
                chatHistory.emit(chatMessage)
            }
        }
    }

    private class ChatServiceImpl(private val chatHistory: MutableSharedFlow<ChatMessage>) : ChatServiceGrpcKt.ChatServiceCoroutineImplBase() {
        override fun chat(requests: Flow<ChatMessage>): Flow<ChatMessage> {
            CoroutineScope(Dispatchers.Default).launch {
                requests.collect { message ->
                    chatHistory.emit(message)
                }
            }
            return chatHistory.asSharedFlow()
        }
    }
}

fun main() {
    val server = ChatServer()
    server.start()
    server.blockUntilShutdown()
}
