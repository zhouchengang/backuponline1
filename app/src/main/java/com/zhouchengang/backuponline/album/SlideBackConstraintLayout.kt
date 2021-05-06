package com.zhouchengang.backuponline.album

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout

class SlideBackConstraintLayout : ConstraintLayout {

    public var onSwipeOff: () -> Unit = {
    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    @SuppressLint("ClickableViewAccessibility")
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        var lastX = 0f
        var lastY = 0f
        rootView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    if (translationX > width * 0.4f) {
                        dismiss()
                    } else {
                        recovery()
                    }
                }
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.rawX
                    lastY = event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    var dx = event.rawX - lastX
                    var dy = event.rawY - lastY
                    moveMethod2(dx, dy)
                    lastX = event.rawX
                    lastY = event.rawY
                    Log.e(
                        "TAG",
                        "onTouch: dx==$dx,dy==$dy,lastX==$lastX,lastY==$lastY,translationX=$translationX"
                    )
                }
            }
            true
        }
    }


    private fun moveMethod2(dx: Float, dy: Float) {
        translationX = if ((translationX + dx) > 0f) (translationX + dx) else 0f
        Log.e("TAG", "translationX=$translationX")
    }


    private fun recovery() {
        ObjectAnimator.ofFloat(this, "translationX", translationX, 0f).apply {
            duration = (200 * translationX / width).toLong()
            repeatCount = 0
            interpolator = AccelerateDecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    translationX = 0f
                }
            })
            start()
        }

    }


    fun dismiss() {
        ObjectAnimator.ofFloat(this, "translationX", translationX, width.toFloat()).apply {
            duration = (200 * (width - translationX) / width).toLong()
            repeatCount = 0
            interpolator = AccelerateDecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    onSwipeOff.invoke()
                }
            })
            start()
        }
    }

}