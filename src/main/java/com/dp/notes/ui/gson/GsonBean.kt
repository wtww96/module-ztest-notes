package com.dp.notes.ui.gson

import com.dp.core.network.gson.IntegerTypeAdapter
import com.dp.core.network.gson.JSONObjectTypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONObject
import java.math.BigDecimal

/**
 * author Dq
 * date on 2022/11/14
 * description Gson容错处理的模拟数据
 */
data class GsonBean(
    val listTest1: List<String>? = null,
    val listTest2: List<String>? = null,
    val listTest3: List<Int>? = null,
    val listTest4: List<Boolean>? = null,

    val booleanTest1: Boolean,
    val booleanTest2: Boolean,
    val booleanTest3: Boolean,
    val booleanTest4: Boolean,
    val booleanTest5: Boolean,
    val booleanTest6: Boolean,

    val stringTest1: String,
    val stringTest2: String,
    val stringTest3: String,
    val stringTest4: String,
    val stringTest5: String,

    val intTest1: Int,
    val intTest2: Int,
    @JsonAdapter(IntegerTypeAdapter::class)
    val intTest3: Int,
    val intTest4: Int,
    val intTest5: Int,

    val longTest1: Long,
    val longTest2: Long,
    val longTest3: Long,
    val longTest4: Long,
    val longTest5: Long,

    val floatTest1: Float,
    val floatTest2: Float,
    val floatTest3: Float,
    val floatTest4: Float,
    val floatTest5: Float,

    val doubleTest1: Double,
    val doubleTest2: Double,
    val doubleTest3: Double,
    val doubleTest4: Double,
    val doubleTest5: Double,

    val bigDecimal1: BigDecimal,
    val bigDecimal2: BigDecimal,
    val bigDecimal3: BigDecimal,

    @SerializedName("bean1_test")
    val bean1: TestBean,
    val bean2: TestBean,
    //@JsonAdapter(JSONObjectTypeAdapter::class)
    //@JsonAdapter(XTypeAdapter::class)
    val bean3: TestBean?,

    val map1: Map<String, String>,
    val map2: Map<String, String>,
    val jsonObject: JSONObject,
    val jsonArray: JSONArray,
)

data class TestBean(
    val number: Int = 666
)