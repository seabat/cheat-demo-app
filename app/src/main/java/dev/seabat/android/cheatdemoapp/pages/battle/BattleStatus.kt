package dev.seabat.android.cheatdemoapp.pages.battle

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.seabat.android.cheatdemoapp.BattleProperty

class BattleStatus(private val battle: BattleProperty) {
    var status: BattleStatusType = BattleStatusType.NOT_START

    private val _braveHp = MutableLiveData<Int>(battle.braveHp)
    val braveHp: LiveData<Int>
        get() = _braveHp

    private var _enemyHp = MutableLiveData<Int>(battle.enemyHp)
    val enemyHp: LiveData<Int>
        get() = _enemyHp

    private var _battleDone = MutableLiveData<Boolean>(false)
    val battleDone: LiveData<Boolean>
        get() = _battleDone

    /**
     * 戦闘をする
     */
    fun battle() {
        if (status == BattleStatusType.NOT_START) {
            status = BattleStatusType.STARTED
        }
    }

    /**
     * 戦闘を終了する
     */
    private fun end() {
        if (status == BattleStatusType.STARTED) {
            status = BattleStatusType.DONE
            _battleDone.value = true
        }
    }

    /**
     * 戦闘をリセットする
     */
    fun reset() {
        status = BattleStatusType.NOT_START
        _battleDone.value = false
        _braveHp.value = battle.braveHp
        _enemyHp.value = battle.enemyHp
    }

    /**
     * 戦闘が終了しているか
     */
    fun isDone(): Boolean {
        return status == BattleStatusType.DONE
    }

    /**
     * 戦闘が開始しているか
     */
    fun isStarted(): Boolean {
        return status == BattleStatusType.STARTED
    }

    /**
     * 敵を攻撃する
     */
    fun attackEnemy(attackPoint: Int) {
        _enemyHp.value = enemyHp.value?.let{
            val remainedHp = it - attackPoint
            Log.d("DEMO", "Enemy remained HP: $remainedHp")
            if (remainedHp > 0) {
                remainedHp
            } else {
                0
            }
        }
        // 戦闘を終了する
        if (_enemyHp.value == 0) {
            end()
        }
    }

    /**
     * 勇者を攻撃する
     */
    fun attackBrave(attackPoint: Int) {
        _braveHp.value = _braveHp.value?.let{
            val remainedHp = it - attackPoint
            Log.d("DEMO", "Brave remained HP: $remainedHp")
            if (remainedHp > 0) {
                remainedHp
            } else {
                0
            }
        }
        // 戦闘を終了する
        if (_braveHp.value == 0) {
            end()
        }
    }
}