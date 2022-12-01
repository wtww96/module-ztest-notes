package com.dp.notes

import com.dp.testinclude.bean.JavaBean
import com.dp.testinclude.bean.KotlinBean
import com.dp.testinclude.service.ITestServiceJava
import com.dp.testinclude.service.ITestServiceKotlin

/**
 * author Dq
 * date on 2022/11/9
 * description 测试include 脚本
 */
object TestIncludeApiManager {

    fun test() {
        val javabean = JavaBean()
        val kotlinBean = KotlinBean("xx")
    }

    val callbackJava = object : ITestServiceJava {
        override fun javaTest() {
        }
    }

    val callbackKotlin = object : ITestServiceKotlin {
        override fun kotlinTest() {

        }
    }
}