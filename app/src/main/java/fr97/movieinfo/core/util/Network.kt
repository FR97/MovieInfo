package fr97.movieinfo.core.util

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast

object Network {

    fun hasInternet(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected

    }

    fun showNoInternetErrorToast(context: Context) {
        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show()
    }

}