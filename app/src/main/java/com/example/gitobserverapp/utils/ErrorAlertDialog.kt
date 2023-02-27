package com.example.gitobserverapp.utils

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

object ErrorAlertDialog {
    fun showDialog(error: String, context: Context){
        val listener = DialogInterface.OnClickListener { _, ok ->
            when (ok){
                AlertDialog.BUTTON_POSITIVE -> {}
            }
        }

        val dialog = android.app.AlertDialog.Builder(context)
            .setCancelable(true)
            .setTitle(error)
            .setPositiveButton("Ok", listener)
            .setOnCancelListener {}
            .create()

        dialog.show()
    }
}