package com.kck.demoatm.application

import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.hideKeyboard(activity: AppCompatActivity) {
    Log.d("Extensions", "hideKeyboard: activity: ${activity::class.java.simpleName}")
    val view = activity.currentFocus
    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view?.windowToken, 0)
}