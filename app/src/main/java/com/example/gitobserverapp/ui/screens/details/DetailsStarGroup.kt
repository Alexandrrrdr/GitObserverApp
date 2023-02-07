package com.example.gitobserverapp.ui.screens.details

import com.example.gitobserverapp.domain.model.StarGroup
import com.example.gitobserverapp.domain.model.User
import java.util.*

data class DetailsStarGroup(
    override val date: Date,
    override val users: List<User>
): StarGroup
