package com.example.gitobserverapp.data.utils.mapper

interface BaseMapper<From, To> {
    fun map(from: From): To
}