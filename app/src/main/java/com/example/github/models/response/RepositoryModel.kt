package com.example.github.models.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RepositoryModel(
    val id: Long?,
    val name: String?,
    val owner: RepositoryOwnerModel?,
    val description: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("forks_count")
    val forksCount: Long?,
    @SerializedName("full_name")
    val fullName: String?,
    @SerializedName("stargazers_count")
    val stars: Long?
) : Serializable
