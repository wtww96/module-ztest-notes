package com.dp.notes.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.dp.notes.R

/**
 * author Dq
 * date on 2023/4/14
 * description
 */
class DefaultDialog : DialogFragment(R.layout.notes_dialog_test) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, com.dp.core.R.style.Theme_Dialog_Base)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    companion object {
        fun show(any: FragmentManager) {
            DefaultDialog().show(any,"xxxxxxxxx")
        }
    }
}