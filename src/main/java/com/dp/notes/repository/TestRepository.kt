package com.dp.notes.repository

import com.dp.core.base.IRepository
import com.dp.core.network.FlowRes
import com.dp.notes.bean.Test1Bean

/**
 * author Dq
 * date on 2022/9/15
 * description
 */
interface TestRepository : IRepository {

    fun requestRx(): FlowRes<Test1Bean>

    fun requestFlow(): FlowRes<Test1Bean>
}

