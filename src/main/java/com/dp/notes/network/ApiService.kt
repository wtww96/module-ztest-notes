package com.dp.notes.network

import com.dp.core.network.Resp
import com.dp.core.network.annotation.UrlName
import com.dp.notes.bean.Test1Bean
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * author Dq
 * date on 2022/10/31
 * description 网络请求接口
 */

interface ApiService {
    //@GET("https://v.juhe.cn/weather/index?format=2&cityname=上海&key=fb06910058de37c52e1273fe7e1f0277")

    //@UrlName("test1")
    @GET("simpleWeather/query?city=上海&key=6359ed4768376c1f109968e64d91e43a")
    fun queryWeatherRx(): Observable<Resp<Test1Bean>>

    @UrlName("weather")
    @GET("weather/index?format=2&cityname=上海&key=fb06910058de37c52e1273fe7e1f0277")
    suspend fun queryWeatherFlow(): Resp<Test1Bean>
}