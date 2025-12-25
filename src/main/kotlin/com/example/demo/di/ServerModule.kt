package com.example.demo.di

import com.example.demo.config.ServerConfig
import com.example.demo.repository.MessageRepository
import com.example.demo.service.ChatServiceImpl
import io.grpc.Server
import io.grpc.ServerBuilder

object ServerModule {
    val config : ServerConfig by lazy{
        ServerConfig.fromEnvironment()
    }

    val messageRepository : MessageRepository by lazy{
        MessageRepository()
    }

    private val chatService : ChatServiceImpl by lazy{
        ChatServiceImpl(messageRepository)
    }

    fun provideServer() : Server {
        return ServerBuilder.forPort(config.port)
            .addService(chatService)
            .build()
    }
}