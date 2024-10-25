package com.dp.notes.network

import com.dp.core.network.annotation.UrlName
import com.dp.core.network.bean.BaseResponse
import com.dp.notes.bean.Test1Bean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers

/**
 * author Dq
 * date on 2022/10/31
 * description 网络请求接口
 */

interface ApiService {
    //@GET("https://v.juhe.cn/weather/index?format=2&cityname=上海&key=fb06910058de37c52e1273fe7e1f0277")
    //@GET("http://apis.juhe.cn/simpleWeather/query?city=上海&key=6359ed4768376c1f109968e64d91e43a")

    //@UrlName("test1")
    //@Headers("Cache-Control: public, max-age=" + 60 * 10)
    @GET("simpleWeather/query?city=上海&key=6359ed4768376c1f109968e64d91e43a")
    fun queryWeatherRx(): Observable<BaseResponse<Test1Bean>>

    @UrlName("weather")
    @GET("weather/index?format=2&cityname=上海&key=fb06910058de37c52e1273fe7e1f0277")
    suspend fun queryWeatherFlow(): BaseResponse<Test1Bean>

    @Headers("Cache-Control: public, max-age=" + 60 * 10)
    @GET("https://api.vshowapi.com/live/list?_random=3pOxQ4tyvbchY8W3PSm3CT0I%2BPQLzIlV&_sign=iC3AN8HcATKBgbKPnb2dZdLj5SXqRYS59vBHFCLYq4eJ0fasO5fbqo%2BbPl0LNl0SLecIBKcEYtBHTJbyDDn%2F6jz7HQWa2mkUDdi4FAyHP928CHwUeBBKyicJsYoBVy5ODJ6l81RBKZaHzWCquBhT5eS7d9u3WOctS1wlYQ%3D%3D&_uid=42452517&p=android&smei_id=BPYGaIAae0qdB9cILVFBDB9Z1sKdS8YNmil%2BIzm133dsAkQ9mS1I02xC%2Fzho6a7uRVD3R9gaqw6hPLzr%2FnZac7A%3D%3D&c=poppo&v=457&l=zh-Hans&mcc=46002&vs=1.3.2.457.0828&uuid=69c1b8c361cb322b")
    suspend fun testRequest(): String

    suspend fun queryWeatherFlowx(): String
}