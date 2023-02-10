package com.example.gitobserverapp.utils.mapper.base

interface BaseMapper<From, To> {
    fun map(from: From): To
}