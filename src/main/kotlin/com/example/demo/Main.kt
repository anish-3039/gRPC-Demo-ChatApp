package com.example.demo

import com.example.demo.client.ChatClient
import com.example.demo.config.ClientConfig
import com.example.demo.di.ServerModule.provideServer
import com.example.demo.server.ChatServer
import com.example.demo.config.ServerConfig
import com.example.demo.di.ServerModule
import com.example.demo.service.ChatServiceImpl
import java.util.Locale
import java.util.Locale.getDefault
import kotlin.system.exitProcess


fun main(args : Array<String>){
        if(args.isEmpty()){
                println("Please provide a command line argument")
                printUsage()
                exitProcess(1)
        }
        val mode : String = args[0].lowercase()
        when(mode){
                "server" -> runServer()
                "client" -> runClient()
                else -> {
                        println("Unknown mode $mode")
                        printUsage()
                }
        }
}

fun runServer() {
        val serverInstance = ServerModule.provideServer()
        val repoInstance = ServerModule.messageRepository

        val chatServer = ChatServer(serverInstance, repoInstance)

        chatServer.start()
        chatServer.blockUntilShutdown()
}


fun runClient() {
        val config = ClientConfig()
        val client = ChatClient(config)
        client.start()
        Thread.currentThread().join()
}

fun printUsage(){
        println(
                """
                        Usage:
                                Server -> To run as Server
                                Client -> To run as Client
                """.trimIndent()
        )
}