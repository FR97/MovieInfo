package fr97.movieinfo.data.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response


class MyInterceptor : Interceptor {

    companion object {
        const val TAG = "NETWORK_TAG"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val t1 = System.nanoTime()
        Log.d(
            TAG, String.format(
                "Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()
            )
        )

        val response = chain.proceed(request)

        val t2 = System.nanoTime()
        Log.d(
            TAG, String.format(
                "Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6, response.headers()
            )
        )

        return response
    }

}