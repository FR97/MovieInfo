package fr97.movieinfo.core.common

import android.app.Application
import android.util.Log
import androidx.work.*
import fr97.movieinfo.core.di.Injector

class App : Application() {

    companion object {
        const val UPDATE_WORK_NAME = "UpdateWork"
    }

    override fun onCreate() {
        super.onCreate()
        val prefs = Injector.preferences(this);
        prefs.observeBoolean("notifications", true).subscribe { notificationsOn ->
            if (!notificationsOn) {
                Log.d("PREFS", "Notifications are off, all canceling work")
                WorkManager.getInstance(this).cancelAllWork()
            } else {
                Log.d("PREFS", "Notifications are on")
            }
        }
    }

}