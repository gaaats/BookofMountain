package com.kongregate.mobile.burritob.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kongregate.mobile.burritob.R
import com.kongregate.mobile.burritob.databinding.FragmentGameBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class GameFragment : Fragment(), GamerInterface {

    var counter = 0.05f
    var diff = 0.05f

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("FragmentStartBinding = null")

    val listLogoEnemy = listOf(
        R.drawable.s1,
        R.drawable.s2,
        R.drawable.s3,
    )

    private var mGameViev: GameUI? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cycleUpAndDovnAlpha()

        binding.btnStart.setOnClickListener {
            mGameViev =
                GameUI(requireContext(), this, R.drawable.snowboard, listLogoEnemy)
            mGameViev!!.setBackgroundResource(R.drawable.phone_snov_1)
            mGameViev!!.background.alpha = 100
            binding.root.addView(mGameViev)
            binding.btnStart.visibility = View.GONE
            binding.tvGameLogo.visibility = View.GONE
            binding.tvScoreGame1.visibility = View.GONE
            binding.imgLogoGame1.visibility = View.GONE
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun closeGame(score: Int) {
        val scoreText = "Score : $score"
        binding.root.removeView(mGameViev)
        binding.tvScoreGame1.text = scoreText
        binding.btnStart.visibility = View.VISIBLE
        binding.tvScoreGame1.visibility = View.VISIBLE
        binding.imgLogoGame1.visibility = View.VISIBLE
        binding.tvGameLogo.visibility = View.VISIBLE
        mGameViev = null

        findNavController().navigate(R.id.action_gameFragment_to_finishFragment)
    }

    private fun cycleUpAndDovnAlpha() {
        lifecycleScope.launch {
            while (true) {
                binding.btnStart.alpha = counter
                if (counter >= 1f) {
                    diff = -0.05f
                }
                if (counter <= 0.1f) {
                    diff = 0.05f
                }
                delay(60)
                counter += diff
            }
        }
    }

}