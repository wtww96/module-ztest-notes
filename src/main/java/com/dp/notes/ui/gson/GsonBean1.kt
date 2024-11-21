package com.dp.notes.ui.gson

/**
 * author Dq
 * date on 2024/8/2
 * description
 */
data class GsonBean1(
    val stringTest1: String? ,
    val intTest1: Int?,
    val longTest1: Long?,
    val name: String ,
    val boolTest1: Boolean?,
    val rewards: List<GsonBeanChild1>? ,
)

data class GsonBeanChild1(
    val name: String = "",
    val boolTest1: Boolean? = null,
)
