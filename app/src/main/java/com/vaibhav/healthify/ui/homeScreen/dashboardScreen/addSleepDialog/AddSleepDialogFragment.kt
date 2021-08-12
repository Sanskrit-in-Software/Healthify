package com.vaibhav.healthify.ui.homeScreen.dashboardScreen.addSleepDialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vaibhav.healthify.databinding.FragmentAddSleepDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AddSleepDialogFragment(private val onTimeSelected: (Int) -> Unit = {}) :
    BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddSleepDialogBinding
    private val viewModel: AddSleepViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddSleepDialogBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectUiState()
        listenToNumberPickers()
        binding.btnSaveTiming.setOnClickListener {
            onTimeSelected(viewModel.getMinutes())
            dismiss()
        }
    }

    private fun listenToNumberPickers() {
        binding.hrsPicker.setOnValueChangedListener { _, _, value ->
            viewModel.onHoursChanged(value)
        }
        binding.minPicker.setOnValueChangedListener { _, _, value ->
            viewModel.onMinutesChanged(value)
        }
    }

    private fun collectUiState() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.uiState.collect {
            binding.btnSaveTiming.isEnabled = it.isButtonEnabled
        }
    }
}
