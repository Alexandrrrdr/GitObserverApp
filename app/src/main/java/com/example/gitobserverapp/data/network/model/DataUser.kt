package com.example.gitobserverapp.data.network.model

import com.example.gitobserverapp.domain.model.User

class DataUser(
    override val login: String,
    override val name: String,
    override val avatarUrl: String
): User