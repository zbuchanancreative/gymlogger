package com.buchanancreative.gymlogger.View.Controls

import android.content.Context
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.buchanancreative.gymlogger.R
import kotlinx.android.synthetic.main.rep_control.view.*

class NumberEntryControl: ConstraintLayout {

    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAtt: Int): super(context, attributeSet, defStyleAtt)

    var value = 0
        private set

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.rep_control, this, false)
        addView(view)
        setupControls()
    }

    fun setLabel(string: String) {
        label.text = string
    }

    private fun setupControls() {
        addReps.setOnClickListener { _ ->
            value++
            editReps.text = SpannableStringBuilder(value.toString())
        }

        minusReps.setOnClickListener { _ ->
            if (value > 0) value--
            editReps.text = SpannableStringBuilder(value.toString())
        }

        editReps.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val editedValue = p0.toString()
                value = if (editedValue != "") editedValue.toInt() else 0
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

    }
}