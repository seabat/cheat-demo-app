package dev.seabat.android.cheatdemoapp

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