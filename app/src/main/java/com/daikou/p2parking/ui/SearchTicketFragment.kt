package com.daikou.p2parking.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.daikou.p2parking.R
import com.daikou.p2parking.base.base.BaseEdittextListener
import com.daikou.p2parking.databinding.FragmentSearchTicketBinding

class SearchTicketFragment : DialogFragment() {

    private var initListener: InitListener? = null
    private var binding: FragmentSearchTicketBinding? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchTicketBinding.inflate(inflater, container, false)

        binding?.txtCancel?.setOnClickListener { dismiss() }
        binding?.txtOk?.setOnClickListener {
            if (initListener != null) {
                binding?.loadingView?.root?.visibility = View.VISIBLE
                initListener?.initCallBack(binding?.ticketNoEdt?.text.toString())
            }
        }

        binding?.ticketNoEdt?.addTextChangedListener(object : BaseEdittextListener() {
            override fun afterTextChangedNotEmpty(editable: Editable) {
                binding?.txtOk?.isEnabled = true
                binding?.txtOk?.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.colorPrimary)
            }

            override fun afterTextChangedIsEmpty() {
                binding?.txtOk?.isEnabled = false
                binding?.txtOk?.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.light_gray)
            }

        })

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val margin = 16
            val inset = InsetDrawable(ColorDrawable(Color.TRANSPARENT), margin)
            dialog.window!!.setBackgroundDrawable(inset)
        }
    }

    fun initDismiss(){
        dismiss()
    }

    fun initDismissProgress() {
        binding?.loadingView?.root?.visibility = View.GONE
    }


    fun setInitListener(initListener: InitListener?) {
        this.initListener = initListener
    }

    interface InitListener {
        fun initCallBack(result : String)
    }
}