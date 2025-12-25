package com.example.demo.config

data class ServerConfig(
    val port: Int,
    val host: String = "0.0.0.0"
){
    companion object{
        //same as using static keyword
        fun fromEnvironment() : ServerConfig = ServerConfig(
            port = System.getenv("PORT")?.toIntOrNull() ?: 50051,
            host = System.getenv("HOST") ?: "0.0.0.0"
        )
    }
}