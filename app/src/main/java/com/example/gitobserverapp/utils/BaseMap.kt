package com.example.gitobserverapp.utils

abstract class BaseMap<From, To> {
    abstract fun map(from: From): To
}