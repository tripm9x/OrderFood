package com.volio.order_food.widget.text.widget.edit_text

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.EditText

import com.volio.order_food.R import com.volio.order_food.widget.TypeFaceUtil


@SuppressLint("AppCompatCustomView","CustomViewStyleable")
class EditTextMedium : EditText {
    constructor(context: Context?) : super(context) {
        this.typeface = TypeFaceUtil.instant!!.medium
        this.setTextColor(ColorUtil.instant!!.colorEditText)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        this.typeface = TypeFaceUtil.instant!!.medium
        initAttr(attrs)
    }

    constructor(
        context: Context?, attrs: AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle) {
        this.typeface = TypeFaceUtil.instant!!.medium
        initAttr(attrs)
    }

    private fun initAttr(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(
            attrs,
            R.styleable.text_style,
            0,
            0
        )
        val textColorDefault = ta.getBoolean(R.styleable.text_style_textColorDefault, true)
        if (textColorDefault) {
            this.setTextColor(ColorUtil.instant!!.colorEditText)
        }

        ta.recycle()
    }
}