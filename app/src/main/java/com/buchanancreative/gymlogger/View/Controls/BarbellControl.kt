package com.buchanancreative.gymlogger.View.Controls

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.buchanancreative.gymlogger.R
import com.buchanancreative.loggerlibrary.Util.Constants
import com.buchanancreative.loggerlibrary.Util.Constants.DEFAULT_BAR_WEIGHT
import kotlinx.android.synthetic.main.barbell_control.view.*

class BarbellControl: ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAtt: Int) : super(context, attributeSet, defStyleAtt)

    var weightSum = DEFAULT_BAR_WEIGHT
    private val barWeight = DEFAULT_BAR_WEIGHT
    private var platesOnBar = arrayListOf<Int>()
    private var platesWidth = 0

    var onWeightUpdateListener: () -> Unit = {}

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.barbell_control, this, false)
        addView(view)

        setupControls()
    }


    private fun setupControls() {
        plate45.setOnClickListener { _ ->
            addPlate(45, R.drawable.profile_45)
        }

        plate35.setOnClickListener { _ ->
            addPlate(35, R.drawable.profile_35)
        }

        plate25.setOnClickListener { _ ->
            addPlate(25, R.drawable.profile_25)

        }

        plate10.setOnClickListener { _ ->
            addPlate(10, R.drawable.profile_10)
        }

        plate5.setOnClickListener { _ ->
            addPlate(5, R.drawable.profile_5)
        }

        plate2_5.setOnClickListener { _ ->
            addPlate(2, R.drawable.profile_2_5)
        }

        clear.setOnClickListener { _ ->
            handleClearPress()
        }
    }

    private fun handleClearPress() {
        removePlates()
        platesOnBar.clear()


        platesWidth = 0
        updateWeight()
    }


    private fun addPlate(weight: Int, drawableId: Int) {
        addLeftPlate(drawableId)
        addRightPlate(drawableId)

        platesOnBar.add(weight)
        updateWeight()

        context.getDrawable(drawableId)?.let {
            platesWidth += it.intrinsicWidth
        }
    }


    private fun addRightPlate(drawableId: Int) {
        val rightPlate = getPlateProfileImageView(drawableId)
        rightPlates.addView(rightPlate)

        animateRightPlate(rightPlate, platesWidth.toFloat())
    }

    private fun getPlateProfileImageView(drawableId: Int): ImageView {
        val params : LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, // This will define text view width
                LinearLayout.LayoutParams.WRAP_CONTENT // This will define text view height
        )
        val imageView = ImageView(context)
        val plateDrawable: Drawable? = context.getDrawable(drawableId)
        imageView.setImageDrawable(plateDrawable)
        imageView.layoutParams = params
        imageView.alpha = 0F

        return imageView
    }

    private fun animateRightPlate(view: ImageView, endingX: Float) {
        val startingX = 300F
        ValueAnimator.ofFloat(startingX, endingX).apply {
            duration = 1000
            addUpdateListener {
                view.x = it.animatedValue as Float
            }
            start()
        }

        ValueAnimator.ofFloat(0F, 1F).apply {
            duration = 900
            addUpdateListener {
                view.alpha = it.animatedValue as Float
            }
            start()
        }
    }

    private fun addLeftPlate(drawableId: Int) {
        val leftPlate = getPlateProfileImageView(drawableId)
        leftPlates.addView(leftPlate)

        val endingX = leftPlates.width -  (platesWidth + leftPlate.drawable.intrinsicWidth)
        animateLeftPlate(leftPlate, endingX.toFloat())
    }

    private fun animateLeftPlate(view: ImageView, endingX: Float) {
        val startingX = -100F
        ValueAnimator.ofFloat(startingX, endingX).apply {
            duration = 1000
            addUpdateListener {
                view.x = it.animatedValue as Float
            }
            start()
        }

        ValueAnimator.ofFloat(0F, 1F).apply {
            duration = 900
            addUpdateListener {
                view.alpha = it.animatedValue as Float
            }
            start()
        }
    }

    private fun removePlates() {
        removeRightPlates()
        removeLeftPlates()
    }

    private fun removeLeftPlates() {
        for (i in platesOnBar.size - 1 downTo 0) {
            val view = leftPlates.getChildAt(i)
            val duration = (1000/(i+1)).toLong()
            animateRemovingPlate(view as ImageView, duration, -500F, leftPlates)
        }
    }

    private fun removeRightPlates() {
        for (i in platesOnBar.size - 1 downTo 0) {
            val view = rightPlates.getChildAt(i)
            val duration = (1000/(i+1)).toLong()
            animateRemovingPlate(view as ImageView, duration, 500F, rightPlates)
        }
    }

    private fun animateRemovingPlate(view: ImageView, dur: Long, endingX: Float, layout: LinearLayout) {
        val startingX = view.x
        ValueAnimator.ofFloat(startingX, endingX).apply {
            duration = dur
            addUpdateListener {
                view.x = it.animatedValue as Float
                if (it.animatedFraction == 1F) {
                    layout.removeView(view)
                }
            }

            start()
        }

        ValueAnimator.ofFloat(1F, 0F).apply {
            duration = 1500
            addUpdateListener {
                view.alpha = it.animatedValue as Float
            }
            start()
        }
    }

    private fun updateWeight() {
        weightSum = platesOnBar.sum() * 2 + barWeight
        onWeightUpdateListener?.invoke()
    }
}