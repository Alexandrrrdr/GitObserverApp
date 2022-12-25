package com.example.gitobserverapp.data.network.model.repo

data class Item(
    val created_at: String,
    val description: String,
    val id: Int,
    val language: String,
    val name: String,
    val owner: Owner,
    val stargazers_count: Int
)