package dev.seabat.android.cheatdemoapp.repositories

import dev.seabat.android.cheatdemoapp.datasources.DefaultSharedPrefs
import dev.seabat.android.cheatdemoapp.datasources.braveWinCount
import dev.seabat.android.cheatdemoapp.datasources.satanWinCount

class ScoreRepository {
    fun loadBraveWinCount(): Int {
        return DefaultSharedPrefs.braveWinCount
    }

    fun saveBraveWinCount(count: Int) {
        DefaultSharedPrefs.braveWinCount = count
    }

    fun loadSatanWinCount(): Int {
        return DefaultSharedPrefs.satanWinCount
    }

    fun saveSatanWinCount(count: Int) {
        DefaultSharedPrefs.satanWinCount = count
    }
}