package com.example.gitobserverapp.data.remote.model

import com.example.gitobserverapp.domain.model.User

class RemoteUser(
    override val login: String,
    override val name: String,
    override val avatarUrl: String
): User