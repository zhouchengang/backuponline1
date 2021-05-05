package com.zhouchengang.backuponline.album

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
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
                    if (translationX > width * 0.1f) {
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
        translationX = (translationX + dx)
        Log.e("TAG", "translationX=$translationX")
    }


    private fun recovery() {
        translationX = 0f
        translationY = 0f
    }


    private fun dismiss() {
        translationX = width.toFloat()
        translationY = 0f
        onSwipeOff.invoke()
    }


}