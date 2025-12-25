package com.example.demo

import com.example.demo.di.ServerModule.provideServer
import com.example.demo.server.ChatServer
import com.example.demo.config.ServerConfig
import com.example.demo.di.ServerModule
import com.example.demo.service.ChatServiceImpl

fun main() {
        val serverInstance = ServerModule.provideServer()
        val repoInstance = ServerModule.messageRepository

        val chatServer = ChatServer(serverInstance, repoInstance)

        chatServer.start()
        chatServer.blockUntilShutdown()
}


