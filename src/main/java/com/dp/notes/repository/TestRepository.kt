package com.dp.notes.repository

import com.dp.core.base.IRepository
import com.dp.core.network.FlowResp
import com.dp.notes.bean.Test1Bean

/**
 * author Dq
 * date on 2022/9/15
 * description
 */
interface TestRepository : IRepository {

    fun requestRx(): FlowResp<Test1Bean>

    fun requestFlow(): FlowResp<Test1Bean>
}

