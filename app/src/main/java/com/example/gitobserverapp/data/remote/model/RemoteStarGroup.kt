package com.example.gitobserverapp.data.remote.model

import com.example.gitobserverapp.domain.model.StarGroup
import com.example.gitobserverapp.domain.model.User
import java.util.*

class RemoteStarGroup(
    override val date: Date,
    override val users: List<User>
): StarGroup