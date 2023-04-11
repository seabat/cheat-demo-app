package dev.seabat.android.cheatdemoapp

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

private const val PREF_NAME = "defaultSharedPrefs"

object DefaultSharedPrefs : SharedPreferences by sharedApplicationContext.getSharedPreferences(
    PREF_NAME, Context.MODE_PRIVATE
)

private const val PREF_BRAVE_WIN_COUNT = "PREF_BLAVE_WIN_COUNT"
var DefaultSharedPrefs.braveWinCount: Int
    get() = getInt(PREF_BRAVE_WIN_COUNT, 0)
    set(value) = edit {
        putInt(PREF_BRAVE_WIN_COUNT, value)
    }

private const val PREF_SATAN_WIN_COUNT = "PREF_SATAN_WIN_COUNT"
var DefaultSharedPrefs.satanWinCount: Int
    get() = getInt(PREF_SATAN_WIN_COUNT, 0)
    set(value) = edit {
        putInt(PREF_SATAN_WIN_COUNT, value)
    }
