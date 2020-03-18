package com.ht117.badgerlib

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import kotlin.math.max

sealed class Shape(val value: Int) {
    object Circle: Shape(1)
    object Rect: Shape(2)
    object Oval: Shape(3)
    object RoundRect: Shape(4)
    object Square: Shape(5)
}

class BadgeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var shape: Shape = Shape.Circle
    private var defTextColor = Color.WHITE
    private var defTextSize: Float = 12f
    private var defBgColor = Color.RED
    private var text: String = ""
    private var badgeGravity = Gravity.END or Gravity.TOP
    var isBinded = false

    private val numberPaint: Paint =
        Paint(Paint.ANTI_ALIAS_FLAG).also {
            it.color = defTextColor
            it.style = Paint.Style.FILL
            it.textSize = defTextSize
            it.textAlign = Paint.Align.CENTER
        }

    private val bgPaint: Paint =
        Paint(Paint.ANTI_ALIAS_FLAG).also {
            it.color = defBgColor
            it.style = Paint.Style.FILL
        }

    init {
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        params.gravity = badgeGravity
        layoutParams = params
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val rectF = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
        val fontMetrics = numberPaint.fontMetrics
        val textHeight = fontMetrics.descent - fontMetrics.ascent
        numberPaint.textSize = textHeight
        when (shape) {
            is Shape.Circle -> {
                canvas?.drawCircle(measuredWidth / 2f, measuredHeight / 2f, measuredWidth / 2f, bgPaint)
                canvas?.drawText(text, measuredWidth / 2f, measuredHeight / 2f + (textHeight / 2f - fontMetrics.descent), numberPaint)
            }
            is Shape.Oval -> {
                canvas?.drawOval(rectF, bgPaint)
                canvas?.drawText(text, measuredWidth / 2f, measuredHeight / 2f + (textHeight / 2f - fontMetrics.descent), numberPaint)
            }
            is Shape.Rect -> {
                canvas?.drawRect(rectF, bgPaint)
                canvas?.drawText(text, measuredWidth / 2f, measuredHeight / 2f + (textHeight / 2f - fontMetrics.descent), numberPaint)
            }
            is Shape.RoundRect -> {
                canvas?.drawRoundRect(rectF, context.dipToPx(5).toFloat(), context.dipToPx(5).toFloat(), bgPaint)
                canvas?.drawText(text, measuredWidth / 2f, measuredHeight / 2f + (textHeight / 2f - fontMetrics.descent), numberPaint)
            }
            is Shape.Square -> {
                val side = Math.min(measuredWidth, measuredHeight)
                val squareF = RectF(0f, 0f, side.toFloat(), side.toFloat())
                canvas?.drawRect(squareF, bgPaint)
                canvas?.drawText(text, side / 2f, side / 2f + (textHeight / 2f - fontMetrics.descent), numberPaint)
            }
        }
    }

    fun setBadgeCount(count: Int) {
        text = count.toString()
        invalidate()
    }

    fun bind(bindedView: View) {
        if (!isBinded) {
            if (parent != null) {
                (parent as ViewGroup).removeView(this)
            }
            if (bindedView.parent is FrameLayout) {
                (bindedView.parent as FrameLayout).addView(this)
                isBinded = true
            } else {
                val oldContainer = bindedView.parent as ViewGroup
                val viewIndex = oldContainer.indexOfChild(bindedView)
                oldContainer.removeView(bindedView)

                val newBadgeFrame = FrameLayout(context).also {
                    it.layoutParams = bindedView.layoutParams
                    it.id = bindedView.id
                    it.addView(bindedView)
                    it.addView(this, layoutParams)
                }

                oldContainer.addView(newBadgeFrame, viewIndex)
                isBinded = true
            }
        }
    }

    fun unbind() {
        if (isBinded && parent != null) {
            (parent as ViewGroup).removeView(this)
            isBinded = false
        }
    }

    class Builder(private val context: Context) {
        private val view: BadgeView = BadgeView(context)

        fun setShape(shape: Shape): Builder {
            view.shape = shape
            return this
        }

        fun setWidthAndHeight(width: Int, height: Int): Builder {
            val params = view.layoutParams as FrameLayout.LayoutParams
            params.width = context.dipToPx(width)
            params.height = context.dipToPx(height)
            view.layoutParams = params
            return this
        }

        fun setWidth(sp: Int): Builder {
            val params = view.layoutParams as FrameLayout.LayoutParams
            params.width = context.dipToPx(sp)
            view.layoutParams = params
            return this
        }

        fun setHeight(sp: Int): Builder {
            val params = view.layoutParams as FrameLayout.LayoutParams
            params.height = context.dipToPx(sp)
            view.layoutParams = params
            return this
        }

        fun setMargin(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0): Builder {
            val params = view.layoutParams as FrameLayout.LayoutParams
            params.leftMargin = max(params.leftMargin, left)
            params.topMargin = max(params.topMargin, top)
            params.rightMargin = max(params.rightMargin, right)
            params.bottomMargin = max(params.bottomMargin, bottom)
            view.layoutParams = params
            return this
        }

        fun setTextSize(size: Int): Builder {
            view.defTextSize = context.spToPx(size.toFloat()).toFloat()
            view.numberPaint.textSize = view.defTextSize
            return this
        }

        fun setTextColor(color: Int): Builder {
            view.defTextColor = color
            view.numberPaint.color = color
            return this
        }

        fun setBgColor(color: Int): Builder {
            view.defBgColor = color
            view.bgPaint.color = color
            return this
        }

        fun setBadgeGravity(gravity: Int): Builder {
            view.badgeGravity = gravity
            val params = view.layoutParams as FrameLayout.LayoutParams
            params.gravity = gravity
            view.layoutParams = params
            return this
        }

        fun build(): BadgeView {
            view.invalidate()
            return view
        }
    }
}