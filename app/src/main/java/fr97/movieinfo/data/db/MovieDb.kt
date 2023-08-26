package fr97.movieinfo.data.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import fr97.movieinfo.core.di.Injector
import fr97.movieinfo.data.db.converter.DateTimeConverter
import fr97.movieinfo.data.entity.MovieEntity


@Database(entities = [MovieEntity::class], version = 2, exportSchema = false)
@TypeConverters(DateTimeConverter::class)
abstract class MovieDb : RoomDatabase() {


    abstract fun movieDao(): MovieDao

    companion object {

        private val TAG = MovieDb::class.java.simpleName
        private const val DATABASE_NAME = "amdb"

        private val LOCK = Any()
        private var instance: MovieDb? = null

        @Synchronized
        fun getInstance(context: Context): MovieDb {
            if (instance == null) {
                Log.d(TAG, "Creating new database instance")
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDb::class.java, DATABASE_NAME
                ).fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            fillDb(context.applicationContext)
                        }
                    }).build()
            }
            return instance ?: throw IllegalStateException("Instance can't be null")
        }

        private fun fillDb(context: Context) {
            Injector.appExecutors.diskIO().execute {
                // TODO Za test podatke
            }
        }
    }

}