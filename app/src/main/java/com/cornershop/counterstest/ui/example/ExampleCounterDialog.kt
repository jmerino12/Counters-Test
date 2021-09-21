package com.cornershop.counterstest.ui.example


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.ExampleCounterBinding


class ExampleCounterDialog : DialogFragment(), OnExampleClick {

    private var _binding: ExampleCounterBinding? = null
    private val binding get() = _binding!!
    private lateinit var exampleAdapterDrinks: ExampleAdapter
    private lateinit var exampleAdapterFoods: ExampleAdapter
    private lateinit var exampleAdapterMisc: ExampleAdapter


    companion object {
        const val TAG = "Example Dialog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            R.style.FullScreenDialog
        )
        exampleAdapterDrinks = ExampleAdapter(this)
        exampleAdapterFoods = ExampleAdapter(this)
        exampleAdapterMisc = ExampleAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ExampleCounterBinding.inflate(inflater, container, false)
        binding.toolbar.setNavigationIcon(R.drawable.ic_back)
        binding.toolbar.setNavigationOnClickListener {
            dismiss()
        }
        val drinks: ArrayList<String> =
            resources.getStringArray(R.array.drinks_array).toList() as ArrayList<String>
        val food: ArrayList<String> =
            resources.getStringArray(R.array.food_array).toList() as ArrayList<String>
        val miscs: ArrayList<String> =
            resources.getStringArray(R.array.misc_array).toList() as ArrayList<String>
        binding.toolbar.title = getString(R.string.examples)
        exampleAdapterDrinks.addHeaderAndSubmitList(drinks)
        exampleAdapterFoods.addHeaderAndSubmitList(food)
        exampleAdapterMisc.addHeaderAndSubmitList(miscs)
        binding.rvDrinks.adapter = exampleAdapterDrinks
        binding.rvFoods.adapter = exampleAdapterFoods
        binding.rvMisc.adapter = exampleAdapterMisc
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(item: String) {
        Toast.makeText(requireContext(), item, Toast.LENGTH_LONG).show()
    }
}