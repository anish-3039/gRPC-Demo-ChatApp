package com.example.demo

import com.example.demo.config.ClientConfig
import com.example.demo.di.ClientModule
import com.example.demo.di.ServerModule
import kotlin.system.exitProcess


fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Please provide a command line argument")
        printUsage()
        exitProcess(1)
    }
    val mode: String = args[0].lowercase()
    when (mode) {
        "server" -> runServer()
        "client" -> runClient()
        else -> {
            println("Unknown mode $mode")
            printUsage()
        }
    }
}

fun runServer() {
    val chatServer = ServerModule.provideChatServer()
    chatServer.start()
    chatServer.blockUntilShutdown()
}

fun runClient() {
    val config = ClientConfig()
    val client = ClientModule.provideChatClient(config)
    client.start()
    Thread.currentThread().join()
}

fun printUsage() {
    println(
        """
            Usage:
                Server -> To run as Server
                Client -> To run as Client
        """.trimIndent()
    )
}