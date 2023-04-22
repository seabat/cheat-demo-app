package dev.seabat.android.cheatdemoapp.page

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.MutableCreationExtras
import dev.seabat.android.cheatdemoapp.Battle
import dev.seabat.android.cheatdemoapp.Constants
import dev.seabat.android.cheatdemoapp.R
import dev.seabat.android.cheatdemoapp.ScoreRepository
import dev.seabat.android.cheatdemoapp.databinding.FragmentBattleBinding

class BattleFragment : Fragment(R.layout.fragment_battle){
    private var binding: FragmentBattleBinding? = null
    private val viewModel: BattleViewModel by viewModels(
        extrasProducer = {
            MutableCreationExtras(defaultViewModelCreationExtras).apply {
                set(
                    BattleViewModel.EXTRA_BATTLE, Battle(
                    braveHp = Constants.BRAVE_HP,
                    braveAttack = Constants.BRAVE_ATTACK,
                    braveDefence = Constants.BRAVE_DEFENCE,
                    enemyHp = Constants.SATAN_HP,
                    enemyAttack = Constants.SATAN_ATTACK,
                    enemyDefence = Constants.SATAN_DEFENCE,
                )
                )
                set(BattleViewModel.EXTRA_REPOSITORY, ScoreRepository())
            }
        },
        factoryProducer = { BattleViewModel.Factory }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBattleBinding.bind(view)

        binding?.buttonTurn?.setOnClickListener{
            binding?.buttonTurn?.isEnabled = false
            viewModel.battle()
        }

        binding?.buttonNext?.setOnClickListener{
            binding?.buttonTurn?.visibility = View.VISIBLE
            binding?.buttonTurn?.isEnabled = true
            binding?.buttonNext?.visibility = View.GONE
            binding?.textBrave?.setTextColor(getColor(requireContext(), R.color.whitesmoke))
            binding?.textBraveHp?.setTextColor(getColor(requireContext(), R.color.whitesmoke))
            binding?.textBraveHpValue?.setTextColor(getColor(requireContext(), R.color.whitesmoke))
            binding?.textBraveAttack?.setTextColor(getColor(requireContext(), R.color.whitesmoke))
            binding?.textBraveAttackValue?.setTextColor(getColor(requireContext(),
                R.color.whitesmoke
            ))
            binding?.textBraveDefence?.setTextColor(getColor(requireContext(), R.color.whitesmoke))
            binding?.textBraveDefenceValue?.setTextColor(getColor(requireContext(),
                R.color.whitesmoke
            ))
            binding?.textCornerman?.text = "戦闘ターンボタンを押してください"
            binding?.imageSatan?.visibility = View.VISIBLE
            viewModel.reset(
                Battle(
                braveHp = Constants.BRAVE_HP,
                braveAttack = Constants.BRAVE_ATTACK,
                braveDefence = Constants.BRAVE_DEFENCE,
                enemyHp = Constants.SATAN_HP,
                enemyAttack = Constants.SATAN_ATTACK,
                enemyDefence = Constants.SATAN_DEFENCE,
            )
            )
        }
        binding?.let {
            setupBraveProperty(it)
            setObserver(it)
        }
    }

    private fun setupBraveProperty(binding: FragmentBattleBinding) {
        binding.textBraveHpValue.text = Constants.BRAVE_HP.toString()
        binding.textBraveDefenceValue.text = Constants.BRAVE_DEFENCE.toString()
        binding.textBraveAttackValue.text = Constants.BRAVE_ATTACK.toString()
    }

    private fun setObserver(binding: FragmentBattleBinding) {
        viewModel.enemyHp.observe(viewLifecycleOwner, Observer {
            vibrate(binding.imageSatan)
            if (it <= 0) {
                binding.textCornerman.text = "魔王を倒した"
                binding.buttonTurn.visibility = View.GONE
                binding.buttonNext.visibility = View.VISIBLE
                binding.imageSatan.visibility = View.INVISIBLE
            }
        })
        viewModel.braveHp.observe(viewLifecycleOwner, Observer {
            binding.textBraveHpValue.text = it.toString()
            if (it <= 0) {
                binding.textCornerman.text = "勇者は◯んでしまった"
                binding.buttonTurn.visibility = View.GONE
                binding.buttonNext.visibility = View.VISIBLE
                binding.textBrave.setTextColor(getColor(requireContext(), R.color.crimson))
                binding.textBraveHp.setTextColor(getColor(requireContext(), R.color.crimson))
                binding.textBraveHpValue.setTextColor(getColor(requireContext(), R.color.crimson))
                binding.textBraveAttack.setTextColor(getColor(requireContext(), R.color.crimson))
                binding.textBraveAttackValue.setTextColor(getColor(requireContext(),
                    R.color.crimson
                ))
                binding.textBraveDefence.setTextColor(getColor(requireContext(), R.color.crimson))
                binding.textBraveDefenceValue.setTextColor(getColor(requireContext(),
                    R.color.crimson
                ))
            }
        })
        viewModel.battleEnable.observe(viewLifecycleOwner, Observer {
            if (it) {
                if (viewModel.braveHp.value!! > 0 && viewModel.enemyHp.value!! > 0) {
                    binding.buttonTurn.isEnabled = true
                }
            }
        })
        viewModel.enemyDamage.observe(viewLifecycleOwner, Observer {
            if (it > -1) {
                binding.textCornerman.text ="魔王に${it}のダメージを与えた"
            }
        })
        viewModel.braveDamage.observe(viewLifecycleOwner, Observer {
            if (it > -1) {
                binding.textCornerman.text ="勇者は${it}のダメージを受けた"
            }
        })
        viewModel.scoreText.observe(viewLifecycleOwner, Observer{
            binding.textScore.text = it
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