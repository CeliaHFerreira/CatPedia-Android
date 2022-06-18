package com.celia.catpedia_android.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.celia.catpedia_android.R
import kotlinx.android.synthetic.main.profile_button.view.*

class ProfileButton: ConstraintLayout {
    @SuppressLint("CustomViewStyleable")
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null
    ): super(context, attrs) {
        LayoutInflater.from(context).inflate(R.layout.profile_button, this, true)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.ProfileButtonAttrs, 0, 0)
            val image = typedArray.getDrawable(R.styleable.ProfileButtonAttrs_profilebutton_image)
            val title = typedArray.getString(R.styleable.ProfileButtonAttrs_profilebutton_text)
            val button = typedArray.getString(R.styleable.ProfileButtonAttrs_profilebutton_btn)
            val background = typedArray.getDrawable(R.styleable.ProfileButtonAttrs_profilebutton_background)
            val textcolor = typedArray.getInt(R.styleable.ProfileButtonAttrs_profilebutton_textcolor, R.color.white)
            typedArray.recycle()

            ivProfileButton.setImageDrawable(image)
            tvTitleProfileButton.text = title
            tvBtnProfileButton.text = button
            tvBtnProfileButton.background = background
            tvBtnProfileButton.setTextColor(textcolor)
        }
    }
}