package com.cornershop.counterstest.ui.addcounter

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.AddCounterBinding
import com.cornershop.counterstest.ui.common.ScopeDialogFragment
import com.cornershop.counterstest.ui.common.UiModel
import com.cornershop.counterstest.ui.common.alert
import com.cornershop.counterstest.ui.common.positiveButton
import com.cornershop.counterstest.ui.example.ExampleCounterDialog
import com.jmb.domain.Counter
import org.koin.androidx.viewmodel.ext.android.viewModel


class AddCounter : ScopeDialogFragment() {

    private var _binding: AddCounterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddCounterViewModel by viewModel()
    private lateinit var listener: OnReloadData

    companion object {
        const val TAG = "Add Counter"
    }

    interface OnReloadData {
        fun onReload(boolean: Boolean = false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            R.style.FullScreenDialog
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewModel.model.observe(this, Observer(::updateUI))
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddCounterBinding.inflate(inflater, container, false)
        binding.toolbar.setNavigationIcon(R.drawable.ic_close)
        binding.toolbar.setNavigationOnClickListener {
            dismiss()
        }
        binding.toolbar.title = getString(R.string.create_counter)
        binding.btnSave.setOnClickListener {
            if (binding.product.text!!.isNotEmpty()) {
                viewModel.addProduct(Counter(null, binding.product.text.toString().trim()))
            }
        }
        binding.counterExample.movementMethod = LinkMovementMethod.getInstance()
        binding.counterExample.setText(
            setClickablePart(getString(R.string.create_counter_disclaimer)),
            TextView.BufferType.SPANNABLE
        )
        return binding.root
    }

    fun updateUI(model: UiModel<List<Counter>>) {
        binding.progress.visibility = if (model is UiModel.Loading) View.VISIBLE else View.GONE
        binding.btnSave.visibility = if (model is UiModel.Loading) View.GONE else View.VISIBLE

        when (model) {
            is UiModel.Content -> {
                listener.onReload(boolean = true)
                dismiss()
            }
            is UiModel.Error -> {
                requireActivity().alert {
                    setTitle(getString(R.string.error_creating_counter_title))
                    setMessage(getString(R.string.connection_error_description))
                    positiveButton(getString(R.string.ok)) { }
                }
                Log.e(tag, model.error.toString())
            }
        }
    }

    private fun setClickablePart(str: String): SpannableStringBuilder {
        val spannableStringBuilder = SpannableStringBuilder(str)
        val index = str.indexOf("See examples")
        spannableStringBuilder.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                val fragment = ExampleCounterDialog()
                fragment.show(
                    childFragmentManager.beginTransaction(),
                    ExampleCounterDialog.TAG
                )
            }
        }, index, str.length, 0)
        return spannableStringBuilder
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as OnReloadData
        } catch (e: Error) {
            Log.e(tag, e.message.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}