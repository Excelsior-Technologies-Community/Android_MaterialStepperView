package com.ext.android_material_stepper_view

import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.withStyledAttributes

class MaterialStepperView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var stepCount = 4
    private var currentStep = 0
    private var stepTitles = emptyArray<String>()

    private var activeColor = 0
    private var completedColor = 0
    private var pendingColor = 0
    private var titleColor = 0
    private var orientationMode = 0

    init {
        context.withStyledAttributes(attrs, R.styleable.MaterialStepperView) {

            stepCount = getInt(R.styleable.MaterialStepperView_stepCount, 4)
            currentStep = getInt(R.styleable.MaterialStepperView_currentStep, 0)

            activeColor = getColor(
                R.styleable.MaterialStepperView_activeColor, 0xFF6200EE.toInt()
            )
            completedColor = getColor(
                R.styleable.MaterialStepperView_completedColor, 0xFF4CAF50.toInt()
            )
            pendingColor = getColor(
                R.styleable.MaterialStepperView_pendingColor, 0xFFBDBDBD.toInt()
            )

            titleColor = getColor(
                R.styleable.MaterialStepperView_titleColor, pendingColor
            )

            orientationMode = getInt(
                R.styleable.MaterialStepperView_stepOrientation, 0
            )

            val titlesId = getResourceId(
                R.styleable.MaterialStepperView_stepTitles, 0
            )
            if (titlesId != 0) {
                stepTitles = resources.getStringArray(titlesId)
            }
        }

        orientation = if (orientationMode == 1) VERTICAL else HORIZONTAL
        buildSteps(animated = false)
    }

    private fun buildSteps(animated: Boolean) {
        removeAllViews()

        repeat(stepCount) { index ->

            val layoutId =
                if (orientationMode == 1)
                    R.layout.stepper_vertical
                else
                    R.layout.stepper_horizontal

            val view = LayoutInflater.from(context).inflate(layoutId, this, false)

            // For horizontal mode, set layout params with weight
            if (orientationMode == 0) {
                view.layoutParams = LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            }

            val circle = view.findViewById<View>(R.id.circle)
            val title = view.findViewById<TextView>(R.id.title)

            val lineLeft = view.findViewById<View?>(R.id.lineLeft)
            val lineRight = view.findViewById<View?>(R.id.lineRight)
            val lineTop = view.findViewById<View?>(R.id.lineTop)
            val lineBottom = view.findViewById<View?>(R.id.lineBottom)

            // ----- Circle State -----
            val circleColor = when {
                index < currentStep -> completedColor  // Completed steps
                index == currentStep -> activeColor     // Current active step
                else -> pendingColor                    // Pending steps
            }
            circle.background = createCircle(circleColor)

            // ----- Lines -----
            if (orientationMode == 0) { // HORIZONTAL
                // Post to set line widths after layout
                view.post {
                    val parentWidth = (view as ViewGroup).width
                    val lineWidth = (parentWidth - 24) / 2 // Half width minus circle

                    lineLeft?.layoutParams = lineLeft?.layoutParams?.apply {
                        width = lineWidth
                    }
                    lineRight?.layoutParams = lineRight?.layoutParams?.apply {
                        width = lineWidth
                    }
                }

                if (index == 0) lineLeft?.visibility = INVISIBLE
                if (index == stepCount - 1) lineRight?.visibility = INVISIBLE

                val lineLeftColor = if (index - 1 < currentStep) completedColor else pendingColor
                lineLeft?.setBackgroundColor(lineLeftColor)

                val lineRightColor = if (index < currentStep) completedColor else pendingColor
                lineRight?.setBackgroundColor(lineRightColor)

            } else { // VERTICAL
                if (index == 0) lineTop?.visibility = INVISIBLE
                if (index == stepCount - 1) lineBottom?.visibility = INVISIBLE

                val lineTopColor = if (index - 1 < currentStep) completedColor else pendingColor
                lineTop?.setBackgroundColor(lineTopColor)

                val lineBottomColor = if (index < currentStep) completedColor else pendingColor
                lineBottom?.setBackgroundColor(lineBottomColor)
            }

            // ----- Title -----
            title.text =
                if (stepTitles.isNotEmpty() && index < stepTitles.size)
                    stepTitles[index]
                else "Step ${index + 1}"
            title.setTextColor(titleColor)

            addView(view)
        }
    }

    // ---- Forward animation ----
    fun next() {
        if (currentStep < stepCount - 1) {
            animateStepTransition(currentStep, currentStep + 1)
        }
    }

    // ---- Backward: instant jump, no animation ----
    fun previous() {
        if (currentStep > 0) {
            currentStep--
            buildSteps(animated = false)
        }
    }

    private fun animateStepTransition(fromStep: Int, toStep: Int) {
        val fromView = getChildAt(fromStep)
        val connectingLine = if (orientationMode == 0) {
            fromView.findViewById<View>(R.id.lineRight)
        } else {
            fromView.findViewById<View>(R.id.lineBottom)
        }
        val circle = fromView.findViewById<View>(R.id.circle)

        connectingLine?.let { line ->
            // Animate the line progress
            val lineAnimator = ValueAnimator.ofInt(0, 100) // 0% to 100%
            lineAnimator.duration = 800
            lineAnimator.addUpdateListener { anim ->
                val fraction = anim.animatedValue as Int / 100f
                val color = interpolateColor(pendingColor, completedColor, fraction)
                line.setBackgroundColor(color)
            }

            lineAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    // Set line fully completed
                    line.setBackgroundColor(completedColor)
                    // Fill the circle after line completes
                    circle.background = createCircle(completedColor)

                    currentStep = toStep
                    buildSteps(animated = false)
                }
            })

            lineAnimator.start()
        } ?: run {
            // Fallback if no line exists
            currentStep = toStep
            buildSteps(animated = false)
        }
    }

    private fun interpolateColor(colorStart: Int, colorEnd: Int, fraction: Float): Int {
        val startA = (colorStart shr 24) and 0xff
        val startR = (colorStart shr 16) and 0xff
        val startG = (colorStart shr 8) and 0xff
        val startB = colorStart and 0xff

        val endA = (colorEnd shr 24) and 0xff
        val endR = (colorEnd shr 16) and 0xff
        val endG = (colorEnd shr 8) and 0xff
        val endB = colorEnd and 0xff

        val a = (startA + ((endA - startA) * fraction)).toInt()
        val r = (startR + ((endR - startR) * fraction)).toInt()
        val g = (startG + ((endG - startG) * fraction)).toInt()
        val b = (startB + ((endB - startB) * fraction)).toInt()

        return (a shl 24) or (r shl 16) or (g shl 8) or b
    }

    private fun createCircle(color: Int) =
        GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(color)
        }
}