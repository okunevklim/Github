package com.example.github.base

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.github.R
import com.example.github.models.response.RepositoryInfoAsString

@Database(entities = [RepositoryInfoAsString::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun repositoriesDao(): RepositoriesDao

    companion object {
        private lateinit var instance: AppDatabase

        fun getDatabase(context: Context): AppDatabase {
            if (!this::instance.isInitialized) {
                instance =
                    Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        context.getString(R.string.package_name)
                    ).build()
            }
            return instance
        }
    }
}
