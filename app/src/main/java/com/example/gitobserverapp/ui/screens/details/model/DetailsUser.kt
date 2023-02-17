package com.example.gitobserverapp.ui.screens.details.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailsUser(
    val id: Int,
    val login: String,
    val avatar_url: String
): Parcelable

