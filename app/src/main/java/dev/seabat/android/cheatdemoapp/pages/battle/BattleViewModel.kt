package dev.seabat.android.cheatdemoapp.pages.battle

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
import dev.seabat.android.cheatdemoapp.BattleProperty
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BattleViewModel(
    private val battleProperty: BattleProperty,
    private val savedStateHandle: SavedStateHandle
    ) : ViewModel() {
    companion object {
        val EXTRA_BATTLE = object : CreationExtras.Key<BattleProperty> {}
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
                return BattleViewModel(battle, savedStateHandle) as T
            }
        }
    }

    private val braveAttack = battleProperty.braveAttack
    private val braveDefence = battleProperty.braveDefence

    private val enemyAttack = battleProperty.enemyAttack
    private val enemyDefence = battleProperty.enemyDefence

    val battleStatus = BattleStatus(battleProperty)

    private var _battleEnable = MutableLiveData<Boolean>(true)
    val battleEnable: LiveData<Boolean>
        get() = _battleEnable

    private var _damageToEnemy = MutableLiveData<Int>(-1)
    val damageToEnemy: LiveData<Int>
        get() = _damageToEnemy

    private var _damageToBrave = MutableLiveData<Int>(-1)
    val damageToBrave: LiveData<Int>
        get() = _damageToBrave

    /**
     * バトルをする
     */
    fun battle() {
        viewModelScope.launch {
            battleStatus.battle()

            delay(100)

            // 勇者の攻撃
            _damageToEnemy.value = braveAttack()
            delay(1500)
            battleStatus.attackEnemy(_damageToEnemy.value!!)

            delay(1000)

            // 敵の攻撃
            if (!battleStatus.isDone()) {
                _damageToBrave.value = enemyAttack()
                delay(1500)
                battleStatus.attackBrave(_damageToBrave.value!!)
            }

            // 戦闘ターン終了
            delay(1000)
            _battleEnable.value = true // 戦闘ターンボタンを押せるようにする
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

    private fun rand(start: Int, end: Int): Int {
        require(start <= end) { "Illegal Argument" }
        return (start..end).random()
    }
}