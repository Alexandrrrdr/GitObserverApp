package com.example.gitobserverapp.ui.screens

interface BaseView {
    fun showLoadPage()
    fun <T> showSuccessPage(list: List<T>?, lastPage: Int?, page: Int?, isLoadAllowed: Boolean?)
    fun showErrorPage(error: String?)
    fun showNetworkErrorPage()
}