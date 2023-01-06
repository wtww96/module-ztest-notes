package com.dp.notes._temp.multi

/**
 * author Dq
 * date on 2023/1/6
 * description RecyclerView 多item bean
 */
data class MultiItemBean(
    val type: Int,
) {

    val span: Int
        get() = when (type) {
            ItemInfo -> 4
            ItemTopBtn -> 1
            ItemCoin -> 4
            ItemBanner -> 4
            ItemMiddleBtn -> 1
            ItemSpace -> 4
            else -> 1
        }

    companion object {
        const val ItemInfo = 1//用户基础信息
        const val ItemTopBtn = 2//顶部按钮列表
        const val ItemCoin = 3//金币View
        const val ItemBanner = 4//banner View
        const val ItemMiddleBtn = 5//中间按钮列表
        const val ItemBottomBtn = 6//底部按钮列表
        const val ItemSpace = 999
    }
}