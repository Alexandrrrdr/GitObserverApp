package com.example.gitobserverapp.domain.mapping

abstract class BaseMap<From, To> {
    abstract fun baseMapping(from: From): To
}