package com.example.plugins

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String,
    val password: String
)

@Serializable
data class Student(
    val username: String,
    val password: String,
    val first_name: String,
    val last_name: String,
    val study_group: String
)

@Serializable
data class Username(
    val username: String
)

@Serializable
data class SetImage(
    val username: String,
    val image: ByteArray
)

@Serializable
data class GetImage(
    val username: String
)
