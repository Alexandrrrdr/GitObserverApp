package com.example.gitobserverapp.ui.screens.barchart.model

import com.example.gitobserverapp.domain.model.StarUser
import java.util.*

data class UiStarGroup(
    override val date: Date,
    override val id: Int,
    override val name: String,
    override val userUrl: String
): StarUser
