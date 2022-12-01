package com.dp.notes.ui.mmkv

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * author Dq
 * date on 2022/11/14
 * description
 */
@Parcelize
data class MmkvBean(
    var name: String
) : Parcelable
