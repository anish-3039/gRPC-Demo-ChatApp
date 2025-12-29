package com.example.demo.client

import com.example.demo.config.ClientConfig
import com.example.demo.grpc.ChatMessage
import com.example.demo.grpc.ChatServiceGrpcKt
import com.example.demo.handler.InputHandler
import com.example.demo.interceptor.JitterInterceptor
import com.example.demo.interceptor.RandomJitterStrategy
import com.example.demo.io.ConsoleIO
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ChatClient(
    private val config: ClientConfig,
    private val consoleIO: ConsoleIO,
    private val inputHandler: InputHandler,
    private val messageHandler: ClientMessageHandler,
    private val messageChannel: Channel<ChatMessage>
) {
    private lateinit var clientName: String
    private val myStrategy = RandomJitterStrategy(1000, 5000)

    private val channel = ManagedChannelBuilder
        .forAddress(config.host, config.port)
        .usePlaintext()
        .intercept(JitterInterceptor(myStrategy))
        .build()

    private val client = ChatServiceGrpcKt.ChatServiceCoroutineStub(channel)

    fun start() {
        clientName = consoleIO.readUsername()

        // Launch coroutine to receive messages from server
        CoroutineScope(Dispatchers.IO).launch {
            val messageStream = client.chat(messageChannel.receiveAsFlow())
            messageHandler.handleIncomingMessages(messageStream, clientName)
        }

        // Launch coroutine to send user input to server
        CoroutineScope(Dispatchers.IO).launch {
            inputHandler.handleInput(clientName)
        }
    }
}

