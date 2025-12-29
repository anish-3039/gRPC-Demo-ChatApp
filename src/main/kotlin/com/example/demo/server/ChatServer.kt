package com.example.demo.server

import com.example.demo.handler.InputHandler
import com.example.demo.io.ConsoleIO
import com.example.demo.lifecycle.ServerLifecycleManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatServer(
    private val lifecycleManager: ServerLifecycleManager,
    private val consoleIO: ConsoleIO,
    private val inputHandler: InputHandler,
    private val messageHandler: ServerMessageHandler
) {
    private lateinit var serverName: String

    fun start() {
        lifecycleManager.start()

        serverName = consoleIO.readUsername()

        // Launch coroutine to handle server's input
        CoroutineScope(Dispatchers.IO).launch {
            inputHandler.handleInput(serverName)
        }

        // Launch coroutine to display incoming messages
        CoroutineScope(Dispatchers.Default).launch {
            messageHandler.handleIncomingMessages(serverName)
        }

        lifecycleManager.registerShutdownHook {
            lifecycleManager.stop()
        }
    }

    fun blockUntilShutdown() {
        lifecycleManager.blockUntilShutdown()
    }
}
