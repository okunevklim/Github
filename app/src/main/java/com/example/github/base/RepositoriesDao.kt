package com.example.github.base

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.github.models.response.RepositoryInfoAsString
import com.example.github.models.response.RepositoryModel
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface RepositoriesDao {
    @androidx.room.Query("SELECT * FROM repositories")
    fun loadAllRepositories(): Maybe<List<RepositoryInfoAsString>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRepository(repository: RepositoryInfoAsString): Completable

    @androidx.room.Query("DELETE FROM repositories")
    fun clearRepositoriesDb(): Completable

    @androidx.room.Query("SELECT COUNT(*) FROM repositories")
    fun getRepositoriesCount(): Maybe<Int>

    @Delete
    fun deleteRepository(repository: RepositoryInfoAsString): Completable
}
