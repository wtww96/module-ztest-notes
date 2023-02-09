package com.dp.notes.ui.mmkv

import android.os.Bundle
import android.util.Log
import com.dp.core.base.BaseActivity
import com.dp.notes.R
import kotlin.random.Random

/**
 * author Dq
 * date on 2022/11/14
 * description mmkv封装使用
 */
class MmkvActivity : BaseActivity(R.layout.notes_activity_mmkv) {

    override fun initView(bundle: Bundle?) {
        Log.e("hehe", "bool1 = ${NotesPreferences.bool}")
        NotesPreferences.bool = true
        Log.e("hehe", "bool2 = ${NotesPreferences.bool}\n")
        //-----------------------------
        Log.e("hehe", "intParam1 = ${NotesPreferences.intParam}")
        NotesPreferences.intParam = 666
        Log.e("hehe", "intParam2 = ${NotesPreferences.intParam}\n")
        //-----------------------------
        Log.e("hehe", "longParam1 = ${NotesPreferences.long_param}")
        NotesPreferences.long_param = 888
        Log.e("hehe", "longParam2 = ${NotesPreferences.long_param}\n")
        //-----------------------------
        Log.e("hehe", "floatparam1 = ${NotesPreferences.floatparam}")
        NotesPreferences.floatparam = 999.99f
        Log.e("hehe", "floatparam2 = ${NotesPreferences.floatparam}\n")
        //-----------------------------
        Log.e("hehe", "double_param_11 = ${NotesPreferences.double_param_1}")
        NotesPreferences.double_param_1 = 1010.12
        Log.e("hehe", "double_param_12 = ${NotesPreferences.double_param_1}\n")
        //-----------------------------
        Log.e("hehe", "strings = ${NotesPreferences.strings}")
        NotesPreferences.strings = "caonima"
        Log.e("hehe", "strings = ${NotesPreferences.strings}\n")
        //-----------------------------
        Log.e("hehe", "parcelable2 = ${NotesPreferences.parcelable2}")
        NotesPreferences.parcelable2 = NotesPreferences.parcelable2.also {
            it.name = "修改name---${Random.nextInt(123)}"
        }
        Log.e("hehe", "parcelable2 = ${NotesPreferences.parcelable2}")
    }
}