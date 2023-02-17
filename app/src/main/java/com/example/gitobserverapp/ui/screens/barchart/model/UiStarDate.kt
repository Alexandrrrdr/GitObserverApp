package com.example.gitobserverapp.ui.screens.barchart.model

import com.example.gitobserverapp.domain.model.StarDate
import java.util.*

data class UiStarDate(
    override val date: Date,
    override val user: UiStarUser
): StarDate
