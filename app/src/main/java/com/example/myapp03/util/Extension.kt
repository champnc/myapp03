package com.example.myapp03.util

import android.app.Dialog
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.myapp03.MainActivity
import com.example.myapp03.R


fun Fragment.getLoading(): Dialog {
    val builder = AlertDialog.Builder((activity as MainActivity))
    builder.setView(R.layout.progress)
    builder.setCancelable(false)
    return builder.create()
}