package com.isanechek.myapplication.screens.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.isanechek.myapplication.onClick
import kotlinx.android.synthetic.main.toolbar_x_layout.*

abstract class BaseScreen : Fragment() {

    abstract fun layoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutId(), container, false)

    fun goToScree(@IdRes screenId: Int) {
        findNavController().navigate(screenId)
    }

    fun closeScreen() {
        findNavController().navigateUp()
    }

    fun setupCloseButton(@DrawableRes resourceId: Int, callback: () -> Unit) {
        with(toolbar_x_back_btn) {
            setImageDrawable(ContextCompat.getDrawable(requireContext(), resourceId))
            onClick {
                callback.invoke()
            }
        }
    }
}