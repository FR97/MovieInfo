package fr97.movieinfo.data.api

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration
import java.util.concurrent.TimeUnit

object Client {

    const val MOVIE_API_URL = "https://api.themoviedb.org/3/"
    const val RESPONSE_CODE_OK = 200
    const val RESPONSE_CODE_AUTH_FAILED = 401

    const val TMDB_API_KEY = "0b0738e374fb08175b44afc907cef644"
    const val LANG = "en_US"

    val retrofitClient: Retrofit by lazy {
       val client = OkHttpClient.Builder()
           .addInterceptor(MyInterceptor())
           .callTimeout(5, TimeUnit.SECONDS)
           .connectTimeout(10, TimeUnit.SECONDS)
           .build()
        Retrofit.Builder()
            .baseUrl(MOVIE_API_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
    }

}