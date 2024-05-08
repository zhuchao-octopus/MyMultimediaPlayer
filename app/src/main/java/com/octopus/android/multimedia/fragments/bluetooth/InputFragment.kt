package com.octopus.android.multimedia.fragments.bluetooth

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.args
import com.octopus.android.multimedia.R
import com.octopus.android.multimedia.databinding.FragmentInputBinding
import com.octopus.android.multimedia.fragments.BaseFragment
import com.octopus.android.multimedia.utils.setOnClickListenerWithInterval
import com.octopus.android.multimedia.utils.viewBinding
import kotlinx.parcelize.Parcelize

@Parcelize
data class InputFragmentParams(
    val resultKey: String = "result",//返回值的key
    val defaultValue: String? = null//文本默认值
) : Parcelable

/**
 * 文本输入页面
 * */
class InputFragment : BaseFragment(R.layout.fragment_input) {

    private val binding: FragmentInputBinding by viewBinding()

    private val args: InputFragmentParams? by args()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editText.setText(args?.defaultValue)
        binding.editText.requestFocus()
        showSoftInput(binding.editText)

        binding.editText.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                confirm()
            }

            false
        }

        binding.left.setOnClickListenerWithInterval {
            findNavController().popBackStack()
        }

        binding.right.setOnClickListenerWithInterval {
            confirm()
        }
    }

    private fun confirm() {
        val navController = findNavController()
        navController.previousBackStackEntry?.savedStateHandle?.set(
            args?.resultKey ?: "result",
            binding.editText.text.toString()
        )

        navController.popBackStack()
    }


    private fun showSoftInput(v: View) {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.showSoftInput(v, 0)
    }
}