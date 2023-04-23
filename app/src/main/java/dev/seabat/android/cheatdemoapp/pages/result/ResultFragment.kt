package dev.seabat.android.cheatdemoapp.pages.result

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dev.seabat.android.cheatdemoapp.R
import dev.seabat.android.cheatdemoapp.repositories.ScoreRepository
import dev.seabat.android.cheatdemoapp.databinding.FragmentResultBinding

class ResultFragment : Fragment(R.layout.fragment_result) {
    private var binding: FragmentResultBinding? = null
    private val viewModel: ResultViewModel by viewModels(
        extrasProducer = {
            MutableCreationExtras(defaultViewModelCreationExtras).apply {
                set(ResultViewModel.EXTRA_REPOSITORY, ScoreRepository())
            }
        },
        factoryProducer = { ResultViewModel.Factory }
    )
    val args: ResultFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentResultBinding.bind(view)

        binding?.let { binding ->
            if (args.winner == "satan") {
                binding.textCornerman?.text = "勇者は◯んでしまった"
                setCrimsonColor(binding)
                viewModel.incrementSatanWin()
            } else {
                binding.textCornerman.text = "魔王を倒した"
                binding.includePropFromResult.imageSatan?.visibility = View.INVISIBLE
                viewModel.incrementBraveWin()
            }

            setListener(binding)
            setObserver(binding)
        }
    }

    private fun setListener(binding: FragmentResultBinding) {
        binding.buttonNext.setOnClickListener{
            findNavController().popBackStack()
        }
    }

    private fun setObserver(binding: FragmentResultBinding) {
        viewModel.scoreText.observe(viewLifecycleOwner, Observer{
            binding.textScore.text = it
        })
    }

    private fun setCrimsonColor(binding: FragmentResultBinding) {
        binding.includePropFromResult.textBrave.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.crimson
            )
        )
        binding.includePropFromResult.textBraveHp.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.crimson
            )
        )
        binding.includePropFromResult.textBraveHpValue.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.crimson
            )
        )
        binding.includePropFromResult.textBraveAttack.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.crimson
            )
        )
        binding.includePropFromResult.textBraveAttackValue.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.crimson
            )
        )
        binding.includePropFromResult.textBraveDefence?.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.crimson
            )
        )
        binding.includePropFromResult.textBraveDefenceValue?.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.crimson
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}