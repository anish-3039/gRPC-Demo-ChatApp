package com.example.demo.di

import com.example.demo.io.ChatMessageFormatter
import com.example.demo.io.ConsoleIO
import com.example.demo.io.MessageFormatter
import com.example.demo.io.StandardConsoleIO

object CommonModule {
    fun provideConsoleIO(): ConsoleIO = StandardConsoleIO()

    fun provideMessageFormatter(): MessageFormatter = ChatMessageFormatter()
}
