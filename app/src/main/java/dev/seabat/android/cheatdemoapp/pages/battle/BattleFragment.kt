package dev.seabat.android.cheatdemoapp.pages.battle

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.navigation.fragment.findNavController
import dev.seabat.android.cheatdemoapp.BattleProperty
import dev.seabat.android.cheatdemoapp.Constants
import dev.seabat.android.cheatdemoapp.R
import dev.seabat.android.cheatdemoapp.databinding.FragmentBattleBinding

class BattleFragment : Fragment(R.layout.fragment_battle){
    private var binding: FragmentBattleBinding? = null
    private val viewModel: BattleViewModel by viewModels(
        extrasProducer = {
            MutableCreationExtras(defaultViewModelCreationExtras).apply {
                set(
                    BattleViewModel.EXTRA_BATTLE, BattleProperty(
                    braveHp = Constants.BRAVE_HP,
                    braveAttack = Constants.BRAVE_ATTACK,
                    braveDefence = Constants.BRAVE_DEFENCE,
                    enemyHp = Constants.SATAN_HP,
                    enemyAttack = Constants.SATAN_ATTACK,
                    enemyDefence = Constants.SATAN_DEFENCE,
                )
                )
            }
        },
        factoryProducer = { BattleViewModel.Factory }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBattleBinding.bind(view)
        binding?.let {
            setupBraveProperty(it)
            setupObserver(it)
            setupListener(it)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun setupBraveProperty(binding: FragmentBattleBinding) {
        binding.includePropFromBattle.textBraveHpValue.text = Constants.BRAVE_HP.toString()
        binding.includePropFromBattle.textBraveDefenceValue.text = Constants.BRAVE_DEFENCE.toString()
        binding.includePropFromBattle.textBraveAttackValue.text = Constants.BRAVE_ATTACK.toString()
    }

    private fun setupListener(binding: FragmentBattleBinding) {
        binding.buttonTurn.setOnClickListener{
            binding.buttonTurn.isEnabled = false
            viewModel.battle()
        }
    }

    private fun setupObserver(binding: FragmentBattleBinding) {
        viewModel.battleStatus.enemyHp.observe(viewLifecycleOwner, Observer {
            if (viewModel.battleStatus.isStarted()) {
                binding.includePropFromBattle.imageSatan.let { it1 -> vibrate(it1) }
            }
        })

        viewModel.battleStatus.braveHp.observe(viewLifecycleOwner, Observer {
            if (viewModel.battleStatus.isStarted()) {
                binding.includePropFromBattle.textBraveHpValue.text = it.toString()
            }
        })

        viewModel.battleEnable.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.buttonTurn.isEnabled = true
            }
        })
        viewModel.damageToEnemy.observe(viewLifecycleOwner, Observer {
            if (viewModel.battleStatus.isStarted()) {
                if (it > -1) {
                    binding.textCornerman.text ="魔王に${it}のダメージを与えた"
                }
            }
        })
        viewModel.damageToBrave.observe(viewLifecycleOwner, Observer {
            if (viewModel.battleStatus.isStarted()) {
                if (it > -1) {
                    binding.textCornerman.text ="勇者は${it}のダメージを受けた"
                }
            }
        })
        viewModel.battleStatus.battleDone.observe(viewLifecycleOwner, Observer {
            if (it) {
                val winner = if (viewModel.battleStatus.enemyHp.value == 0) {
                    "brave"
                } else {
                    "satan"
                }
                val action = BattleFragmentDirections.actionBattleToResult().setWinner(winner)
                findNavController().navigate(action)
                viewModel.battleStatus.reset()
            }
        })
    }

    private fun vibrate(target: View) {
        // NOTE: https://dev.classmethod.jp/articles/android-prop-animation/

        val animatorList: MutableList<Animator> = ArrayList()

        // 移動距離(translationX)は、開始位置から左右に移動距離を100, 70, 50, 40...と徐々に減らしていく
        // 前のアニメーションと繋がるように終了位置(第4引数)の値を開始位置(第3引数)として設定する

        // 時間(duration)は10, 30, 60, 100...と徐々に増やしていく
        animatorList.add(ObjectAnimator.ofFloat(target, "translationX", 0f, -100f).setDuration(10))
        animatorList.add(ObjectAnimator.ofFloat(target, "translationX", -100f, 70f).setDuration(30))
        animatorList.add(ObjectAnimator.ofFloat(target, "translationX", 70f, -50f).setDuration(60))
        animatorList.add(ObjectAnimator.ofFloat(target, "translationX", -50f, 40f).setDuration(100))
        animatorList.add(ObjectAnimator.ofFloat(target, "translationX", 40f, -30f).setDuration(150))
        animatorList.add(ObjectAnimator.ofFloat(target, "translationX", -30f, 0f).setDuration(210))

        val animationSet = AnimatorSet()
        animationSet.playSequentially(animatorList) // 順番にアニメーションを実施
        animationSet.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}