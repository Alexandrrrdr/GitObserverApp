//package com.example.gitobserverapp.data.remote
//
//import com.example.gitobserverapp.data.utils.Constants.GENERAL_NETWORK_ERROR
//import com.example.gitobserverapp.data.utils.mapper.BaseMapper
//import com.example.gitobserverapp.data.utils.mapper.DomainMapper
//import com.example.gitobserverapp.data.utils.mapper.Mapper
//import com.example.gitobserverapp.domain.model.*
//import retrofit2.Response
//import java.io.IOException
//
//inline fun <T : Any> Response<T>.onSuccess(action: (T) -> Unit): Response<T> {
//    if (isSuccessful) body()?.run(action)
//    return this
//}
//inline fun <T : Any> Response<T>.onFailure(action: (HttpError) -> Unit) {
//    if (!isSuccessful) errorBody()?.run { action(HttpError(Throwable(message()), code())) }
//}
//inline fun <T : Any> Response<T>.onException(action: (ExceptionError) -> Unit) {
//    if (!isSuccessful) errorBody()?.run { action(ExceptionError(Throwable(message()))) }
//}
//
//fun <T : Mapper<R>, R : Any> Response<T>.getApi(): ResultD<R> {
//    try {
//        onSuccess { return Success(it.map()) }
//        onFailure { return Failure(it) }
//        onException { return IoException(it) }
//        return Failure(HttpError(Throwable(GENERAL_NETWORK_ERROR)))
//    } catch (e: IOException) {
//        return IoException(ExceptionError(Throwable(GENERAL_NETWORK_ERROR)))
//    }
//}