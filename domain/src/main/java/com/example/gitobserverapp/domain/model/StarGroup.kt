package com.example.gitobserverapp.domain.model

import com.squareup.moshi.Json
import java.util.*

data class StarGroup(
    val date: Date,
    val users: StarUser
)

data class StarUser(
    val id: Int,
    val name: String,
    val userAvaUrl: String
)