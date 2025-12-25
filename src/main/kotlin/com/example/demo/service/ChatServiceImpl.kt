package com.example.demo.service

import com.example.demo.grpc.ChatMessage
import com.example.demo.grpc.ChatServiceGrpcKt
import com.example.demo.repository.MessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ChatServiceImpl(private val messageRepository: MessageRepository) :
    ChatServiceGrpcKt.ChatServiceCoroutineImplBase() {
    override fun chat(requests: Flow<ChatMessage>): Flow<ChatMessage> = channelFlow{
        launch{
            requests.collect { message ->
                messageRepository.broadcast(message)
            }
        }

        messageRepository.messages.collect { message ->
            send(message)
        }
    }
}
