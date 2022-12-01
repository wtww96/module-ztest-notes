package com.dp.notes.bean

/**
 * author Dq
 * date on 2022/9/16
 * description
 */
data class TestBean(
    val reason: String,
    val result: Result?,
    val resultcode: String,
)

data class Result(
    val future: List<Future>,
    val sk: Sk,
    val today: Today,
)

data class Future(
    val date: String,
    val direct: String,
    val temperature: String,
    val weather: String,
    val weather_id: WeatherId,
    val week: String,
    val wind: String,
    val wid: Wid
)

data class Sk(
    val humidity: String,
    val temp: String,
    val time: String,
    val wind_direction: String,
    val wind_strength: String,
)

data class Today(
    val city: String,
    val comfort_index: String,
    val date_y: String,
    val dressing_advice: String,
    val dressing_index: String,
    val drying_index: String,
    val exercise_index: String,
    val temperature: String,
    val travel_index: String,
    val uv_index: String,
    val wash_index: String,
    val weather: String,
    val weather_id: WeatherId,
    val week: String,
    val wind: String,
)

data class WeatherId(
    val fa: String,
    val fb: String,
)