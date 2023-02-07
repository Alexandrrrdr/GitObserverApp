package com.example.gitobserverapp.utils

interface BaseMap<From, To> {
    fun map(from: From): To
}