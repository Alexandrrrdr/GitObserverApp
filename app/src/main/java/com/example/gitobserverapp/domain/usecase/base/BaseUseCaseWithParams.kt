package com.example.gitobserverapp.domain.usecase.base

interface BaseUseCaseWithParams<X, Y, T, Z> {
    suspend fun getData(value_one: X, value_two: Y, value_three: T) : Z
}