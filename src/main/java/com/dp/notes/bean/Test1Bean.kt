package com.dp.notes.bean

/**
 * author Dq
 * date on 2022/9/16
 * description
 */
data class Test1Bean(
    val city: String,
    val future: List<Future>,
    val realtime: Realtime,
) {
    val text: String
        get() = "$city , ${realtime.info} , ${realtime.direct}"
}

data class Realtime(
    val aqi: String,
    val direct: String,
    val humidity: String,
    val info: String,
    val power: String,
    val temperature: String,
    val wid: String,
) {
    constructor() : this("", "", "", "", "", "", "")
}

data class Wid(
    val day: String,
    val night: String,
)