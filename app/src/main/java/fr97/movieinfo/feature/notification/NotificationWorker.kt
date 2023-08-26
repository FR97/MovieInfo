package fr97.movieinfo.feature.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.WorkerParameters
import fr97.movieinfo.R
import fr97.movieinfo.core.extension.vectorToBitmap
import fr97.movieinfo.feature.main.MainActivity
import java.lang.Exception
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NotificationWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {


    override suspend fun doWork(): Result {
        val movieId = inputData.getInt("movieId", 0)
        val movieName = inputData.getString("movieTitle") ?: ""
        val releaseDateStr = inputData.getString("releaseDate") ?: ""
        val releaseDate = LocalDate.parse(releaseDateStr)
        val formattedDate = releaseDate.format(DateTimeFormatter.ofPattern("dd MM uuuu"))
        return try {
            sendNotification(
                movieId,
                "$movieName will be released soon",
                "Movie $movieName is coming out on $formattedDate"
            )
            Log.d("Notifications", "Notification is sent")
            Result.success()
        } catch (e: Exception) {
            Log.d("Notifications", "Notification failed")
            Result.failure()
        }
    }


    private fun sendNotification(id: Int, title: String, subtitle: String) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(NOTIFICATION_ID, id)

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val bitmap = applicationContext.vectorToBitmap(R.drawable.ic_schedule_black)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)
            .setLargeIcon(bitmap).setSmallIcon(R.drawable.ic_schedule_white)
            .setContentTitle(title).setContentText(subtitle)
            .setDefaults(NotificationCompat.DEFAULT_ALL).setContentIntent(pendingIntent).setAutoCancel(true)

        notification.priority = NotificationCompat.PRIORITY_MAX

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(NOTIFICATION_CHANNEL)

            val ringtoneManager = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

            val channel =
                NotificationChannel(
                    NOTIFICATION_CHANNEL,
                    NOTIFICATION_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )

            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            channel.setSound(ringtoneManager, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(id, notification.build())
    }

    companion object {
        const val NOTIFICATION_ID = "MovieInfo_notification"
        const val NOTIFICATION_NAME = "MovieInfo"
        const val NOTIFICATION_CHANNEL = "MovieInfo__channel_01"
        const val NOTIFICATION_WORK = "MovieInfo_notification_work"

        fun getConstraints(): Constraints {
            return Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresCharging(true)
                .setRequiresDeviceIdle(true)
                .build()
        }

    }


}