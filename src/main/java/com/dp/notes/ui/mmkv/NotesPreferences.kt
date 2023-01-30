package com.dp.notes.ui.mmkv

import com.dp.core.extension.*

/**
 * author Dq
 * date on 2022/11/14
 * description
 */
object NotesPreferences {
    var bool by mmkv.boolean()

    var intParam by mmkv.int(0)

    var long_param by mmkv.long(8L)

    var floatparam by mmkv.float(80.0F)

    var double_param_1 by mmkv.double(0.0)

    var strings by mmkv.string("傻逼")

    var parcelable1 by mmkv.parcelable<MmkvBean>()
    var parcelable2 by mmkv.parcelable(MmkvBean("xx"))
}