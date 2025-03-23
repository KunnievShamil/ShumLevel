package ru.codesh.shumlevel.core.util

import android.content.Context

fun Context.saveFlag(key: String, value: Boolean) {
    getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        .edit()
        .putBoolean(key, value)
        .apply()
}

fun Context.getFlag(key: String, default: Boolean = false): Boolean {
    return getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        .getBoolean(key, default)
}