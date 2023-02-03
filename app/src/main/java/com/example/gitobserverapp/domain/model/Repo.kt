package com.example.gitobserverapp.domain.model

import java.util.*

interface Repo {

    val name: String

    val createdDate: Date

    val starsCount: Int

    val owner: User

}