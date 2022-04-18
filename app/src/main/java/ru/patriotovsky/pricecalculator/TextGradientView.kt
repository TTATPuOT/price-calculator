package ru.patriotovsky.pricecalculator

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet


class TextGradientView(context: Context, attrs: AttributeSet) :
    androidx.appcompat.widget.AppCompatTextView(context, attrs) {
    private val a = context.obtainStyledAttributes(attrs, R.styleable.TextGradientView)

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (changed) {
            paint.shader = LinearGradient(
                0f, 0f, width.toFloat(), height.toFloat(),
                a.getColor(R.styleable.TextGradientView_startColor, 0),
                a.getColor(R.styleable.TextGradientView_endColor, 0),
                Shader.TileMode.CLAMP
            )
        }
    }
}