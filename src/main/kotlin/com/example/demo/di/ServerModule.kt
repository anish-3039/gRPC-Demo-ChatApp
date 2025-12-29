package com.example.demo.di

import com.example.demo.config.ServerConfig
import com.example.demo.handler.ServerInputHandler
import com.example.demo.lifecycle.GrpcServerLifecycleManager
import com.example.demo.repository.MessageRepository
import com.example.demo.server.ChatServer
import com.example.demo.server.ServerMessageHandler
import com.example.demo.service.ChatServiceImpl
import io.grpc.Server
import io.grpc.ServerBuilder

object ServerModule {
    val config: ServerConfig by lazy {
        ServerConfig.fromEnvironment()
    }

    val messageRepository: MessageRepository by lazy {
        MessageRepository()
    }

    private val chatService: ChatServiceImpl by lazy {
        ChatServiceImpl(messageRepository)
    }

    private fun provideGrpcServer(): Server {
        return ServerBuilder.forPort(config.port)
            .addService(chatService)
            .build()
    }

    fun provideChatServer(): ChatServer {
        val grpcServer = provideGrpcServer()
        val lifecycleManager = GrpcServerLifecycleManager(grpcServer)
        val consoleIO = CommonModule.provideConsoleIO()
        val inputHandler = ServerInputHandler(consoleIO, messageRepository)
        val messageHandler = ServerMessageHandler(
            consoleIO,
            CommonModule.provideMessageFormatter(),
            messageRepository
        )

        return ChatServer(lifecycleManager, consoleIO, inputHandler, messageHandler)
    }
}