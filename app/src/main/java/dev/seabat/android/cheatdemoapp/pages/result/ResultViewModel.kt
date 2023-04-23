package dev.seabat.android.cheatdemoapp.pages.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import dev.seabat.android.cheatdemoapp.repositories.ScoreRepository

class ResultViewModel(
    private val scoreRepository: ScoreRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    companion object {
        val EXTRA_REPOSITORY = object : CreationExtras.Key<ScoreRepository> {}
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()
                // viewModels(extrasProducer,) で受け取った extras からオブジェクトを取り出す
                val scoreRepository = extras[EXTRA_REPOSITORY]!!

                return ResultViewModel( scoreRepository, savedStateHandle) as T
            }
        }
    }

    private val _scoreText : MutableLiveData<String> by lazy {
        MutableLiveData<String>(createScoreText())
    }
    val scoreText: LiveData<String>
        get() = _scoreText

    fun incrementBraveWin() {
        val newCount = scoreRepository.loadBraveWinCount() + 1
        scoreRepository.saveBraveWinCount(newCount)
        _scoreText.value = createScoreText()
    }

    fun incrementSatanWin() {
        val newCount = scoreRepository.loadSatanWinCount() + 1
        scoreRepository.saveSatanWinCount(newCount)
        _scoreText.value = createScoreText()
    }

    private fun createScoreText(): String {
        return "勇者${scoreRepository.loadBraveWinCount()}勝 : 魔王${scoreRepository.loadSatanWinCount()}勝"
    }
}