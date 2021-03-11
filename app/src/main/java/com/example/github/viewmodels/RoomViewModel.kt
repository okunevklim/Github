package com.example.github.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.github.base.AppDatabase
import com.example.github.models.response.RepositoryInfoAsString
import com.example.github.models.response.RepositoryModel
import io.reactivex.Completable
import io.reactivex.Maybe

class RoomViewModel(application: Application) : AndroidViewModel(application) {
    private var database: AppDatabase = AppDatabase.getDatabase(application)

    fun insertRepository(repository: RepositoryInfoAsString): Completable {
        return database.repositoriesDao().insertRepository(repository)
    }

    fun deleteRepository(repository: RepositoryInfoAsString): Completable {
        return database.repositoriesDao().deleteRepository(repository)
    }

    fun loadAllRepositories(): Maybe<List<RepositoryInfoAsString>> {
        return database.repositoriesDao().loadAllRepositories()
    }

    fun getCountRepositories(): Maybe<Int> {
        return database.repositoriesDao().getRepositoriesCount()
    }

    fun clearRepositoriesDb(): Completable {
        return database.repositoriesDao().clearRepositoriesDb()
    }

}