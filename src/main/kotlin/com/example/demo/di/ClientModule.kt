package com.example.demo.di

import com.example.demo.client.ChatClient
import com.example.demo.client.ClientMessageHandler
import com.example.demo.config.ClientConfig
import com.example.demo.grpc.ChatMessage
import com.example.demo.handler.ClientInputHandler
import kotlinx.coroutines.channels.Channel

object ClientModule {
    fun provideChatClient(config: ClientConfig): ChatClient {
        val consoleIO = CommonModule.provideConsoleIO()
        val messageChannel = Channel<ChatMessage>(Channel.UNLIMITED)
        val inputHandler = ClientInputHandler(consoleIO, messageChannel)
        val messageHandler = ClientMessageHandler(
            consoleIO,
            CommonModule.provideMessageFormatter()
        )

        return ChatClient(config, consoleIO, inputHandler, messageHandler, messageChannel)
    }
}
