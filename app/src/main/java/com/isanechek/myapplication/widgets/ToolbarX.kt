package com.isanechek.myapplication.widgets

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.animation.DecelerateInterpolator
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import com.isanechek.myapplication.R

open class ToolbarX : Toolbar {

    private var isElevationShown = false
    private var animationDuration = 0L
    private var toolbarElevation = 8f

    constructor(context: Context) : super(context) {
        initView(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs, defStyleAttr)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setElevationVisibility(show: Boolean) {
        if (isElevationShown == show) {
            return
        }


        Log.e("Hyi", "Is show $show")

        isElevationShown = show

        val z = if (show) toolbarElevation else 0f
        Log.e("Hyi", "z $z")
        ViewCompat.animate(this).run {
            translationZ(z)
            interpolator = DecelerateInterpolator()
            duration = animationDuration
            start()
        }
    }

    private fun initView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        if (isInEditMode || attrs == null) {
            return
        }

        with(context.obtainStyledAttributes(attrs, R.styleable.ToolbarX, defStyleAttr, 0)) {
            animationDuration = getInt(R.styleable.ToolbarX_toolbarX_animationDuration, 250).toLong()
            isElevationShown = getBoolean(R.styleable.ToolbarX_toolbarX_showElevationAtStart, false)
            recycle()
        }

        with(ViewCompat.getElevation(this)) {
            toolbarElevation = when {
                this == 0f -> resources.getDimension(R.dimen.dp16)
                else -> this
            }
        }

        if (!isElevationShown) {
            ViewCompat.setElevation(this, 0f)
        }
    }
}