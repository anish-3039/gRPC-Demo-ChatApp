package com.example.demo.io

import java.util.Scanner

class StandardConsoleIO : ConsoleIO {
    private val scanner = Scanner(System.`in`)

    override fun readLine(): String = scanner.nextLine()

    override fun writeLine(message: String) {
        println(message)
    }

    override fun readUsername(): String {
        writeLine("Enter your name:")
        return readLine()
    }
}
