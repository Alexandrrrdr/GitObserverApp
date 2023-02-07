package com.example.gitobserverapp.ui.screens.main

import com.example.gitobserverapp.domain.model.Repo
import com.example.gitobserverapp.domain.model.User

class MainRepo(
    override val name: String,
    override val starsCount: Int,
    override val owner: User,
    override val id: Int
): Repo {
}