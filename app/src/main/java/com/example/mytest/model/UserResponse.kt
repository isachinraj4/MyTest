package com.example.mytest.model

data class UserResponse(
    val responseList: List<List<String>> = listOf(listOf("")),
    val userSelectedOption: String = "",
    val correctOption: String = "",
    val showResponse: Boolean = false
)
