package com.cornershop.counterstest.ui.addcounter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.AddCounterBinding

class AddCounter : DialogFragment() {

    private var _binding: AddCounterBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "Add Counter"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            DialogFragment.STYLE_NORMAL,
            R.style.FullScreenDialog
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = AddCounterBinding.inflate(inflater, container, false)
        binding.toolbar.setNavigationIcon(R.drawable.ic_close)
        binding.toolbar.setNavigationOnClickListener {
            dismiss()
        }
        binding.toolbar.title = getString(R.string.create_counter)
        binding.btnSave.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Evento ocurrió exitosamente!",
                Toast.LENGTH_SHORT
            ).show()
            dismiss()
        }
        return binding.root
    }
}