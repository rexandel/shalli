package com.rexandel.shalli

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rexandel.shalli.databinding.FragmentAddCityCardBinding

class AddCityCardFragment : Fragment() {

    private var _binding: FragmentAddCityCardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCityCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.confirmButton.setOnClickListener {
            hideKeyboard()

            val cityName = binding.cityNameInput.text.toString().trim()

            if (cityName.isNotEmpty()) {
                (activity as? MainActivity)?.addCityCard(cityName)
            }

            closeFragmentWithAnimation()
        }

        binding.cancelButton.setOnClickListener {
            hideKeyboard()
            closeFragmentWithAnimation()
        }

        binding.root.setOnClickListener {
            hideKeyboard()
        }

        binding.cityNameInput.requestFocus()
        binding.cityNameInput.postDelayed({
            showKeyboard()
        }, 100)
    }

    private fun closeFragmentWithAnimation() {
        (activity as? MainActivity)?.hideDialogContainer()
    }

    private fun hideKeyboard() {
        (activity as? MainActivity)?.hideKeyboard()
    }

    private fun showKeyboard() {
        (activity as? MainActivity)?.showKeyboard(binding.cityNameInput)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddCityCardFragment()
    }
}