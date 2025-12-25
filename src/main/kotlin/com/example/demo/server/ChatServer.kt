package com.example.demo.server

import com.example.demo.grpc.ChatMessage
import com.example.demo.repository.MessageRepository
import com.example.demo.service.ChatServiceImpl
import io.grpc.ServerBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import io.grpc.Server
import java.util.Scanner

class ChatServer (
    private val server : Server,
    private val repository : MessageRepository,
){
    private lateinit var serverName : String
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
            repository.messages.collect { message ->
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

                repository.broadcast(chatMessage)
            }
        }
    }
}
