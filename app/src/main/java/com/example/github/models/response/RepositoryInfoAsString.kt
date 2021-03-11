package com.example.github.models.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "repositories")
data class RepositoryInfoAsString(
    @PrimaryKey(autoGenerate = false)
    val id: Long?,
    @ColumnInfo(name = "content")
    val content: String?
) : Serializable