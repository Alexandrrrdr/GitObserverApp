package com.example.gitobserverapp.domain.model

import java.util.*

interface Repo {
    val id: Int
    val description: String?
    val name: String
    val owner: RepoOwner
    val created: Date
    val starUserAmount: Int
}