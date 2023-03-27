package com.dp.notes.ui.gson

import android.os.Bundle
import android.util.Log
import com.dp.core.base.BaseActivity
import com.dp.core.extension.clickEvent
import com.dp.core.extension.fromJson
import com.dp.core.extension.toJson
import com.dp.core.util.CoreUtil
import com.dp.core.viewbinding.bindings
import com.dp.core.windowinsets.fitStatusBar
import com.dp.notes.R
import com.dp.notes.databinding.NotesActivityGsonBinding

/**
 * author Dq
 * date on 2022/11/14
 * description Gson容错处理测试
 */
class GsonActivity : BaseActivity(R.layout.notes_activity_gson) {
    private val binding by bindings<NotesActivityGsonBinding>()

    override fun initView(bundle: Bundle?) {
        fitStatusBar()
    }

    override fun initListener() {
        binding.button.clickEvent {
            val str = CoreUtil.instance.getJson("gsonStr1.json").replace("stringTest3","xxxxx")
            val bean = str.fromJson<GsonBean>()
            Log.e("hehe", "bean1 = ${bean.toJson()}")
            Log.e("hehe", "bean2 = ${bean}")
            binding.logText.add("stringTest4 = ${bean.stringTest4}  stringTest5 = ${bean.stringTest5}")
            Log.e("hehe", "stringTest1 = ${bean.stringTest1} ")
            Log.e("hehe", "stringTest4 = ${bean.stringTest4}  stringTest5 = ${bean.stringTest5}")
            Log.e("hehe", "longTest3 = ${bean.longTest3}  longTest4 = ${bean.longTest4}")
            Log.e("hehe", "stringTest3 = ${bean.stringTest3}  ")
        }

        binding.button2.clickEvent {

        }
    }
}