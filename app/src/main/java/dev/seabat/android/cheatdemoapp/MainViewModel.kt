package dev.seabat.android.cheatdemoapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(private val battle: Battle, private val savedStateHandle: SavedStateHandle) : ViewModel() {
    companion object {
        val EXTRA_BATTLE = object : CreationExtras.Key<Battle> {}

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()
                // viewModels(extrasProducer,) で受け取った extras からオブジェクトを取り出す
                val battle = extras[EXTRA_BATTLE]!!

                return MainViewModel(battle, savedStateHandle) as T
            }
        }
    }

    private val _braveHp = MutableLiveData<Int>(battle.braveHp)
    val braveHp: LiveData<Int>
        get() = _braveHp
    private val braveAttack = battle.braveAttack
    private val braveDefence = battle.braveDefence

    private val _enemyHp = MutableLiveData<Int>(battle.enemyHp)
    val enemyHp: LiveData<Int>
        get() = _enemyHp
    private val enemyAttack = battle.enemyAttack
    private val enemyDefence = battle.enemyDefence

    private val _battleEnable = MutableLiveData<Boolean>(true)
    val battleEnable: LiveData<Boolean>
        get() = _battleEnable

    private val _enemyDamage = MutableLiveData<Int>(-1)
    val enemyDamage: LiveData<Int>
        get() = _enemyDamage

    private val _braveDamage = MutableLiveData<Int>(-1)
    val braveDamage: LiveData<Int>
        get() = _braveDamage

    fun battle() {
        viewModelScope.launch {
            delay(100)
            // 勇者の攻撃
            val braveAttackPoint = braveAttack()
            _enemyDamage.value = braveAttackPoint
            delay(1500)
            _enemyHp.value = enemyHp.value?.let{
                val remainedHp = it - braveAttackPoint
                Log.d("DEMO", "Enemy remained HP: $remainedHp")
                if (remainedHp > 0) {
                    remainedHp
                } else {
                    0
                }
            }

            delay(1000)

            // 敵の攻撃
            if (enemyHp.value != null && enemyHp.value!! > 0) {
                val enemyAttackPoint = enemyAttack()
                _braveDamage.value = enemyAttackPoint
                delay(1500)
                _braveHp.value = _braveHp.value?.let{
                    val remainedHp = it - enemyAttackPoint
                    Log.d("DEMO", "Brave remained HP: $remainedHp")
                    if (remainedHp > 0) {
                        remainedHp
                    } else {
                        0
                    }
                }
            }

            // 戦闘ターン終了
            delay(1000)
            _battleEnable.value = true
        }
    }

    private fun braveAttack(): Int {
        val result = braveAttack + rand(0, 10) - enemyDefence
        Log.d("DEMO", "Brave attack point: $result")
        return if (result > 0) {
            result
        } else {
            0
        }
    }

    private fun enemyAttack(): Int {
        val result = enemyAttack + rand(0, 10) - braveDefence
        Log.d("DEMO", "Enemy attack point: $result")
        return if (result > 0) {
            result
        } else {
            0
        }
    }

    fun rand(start: Int, end: Int): Int {
        require(start <= end) { "Illegal Argument" }
        return (start..end).random()
    }
}