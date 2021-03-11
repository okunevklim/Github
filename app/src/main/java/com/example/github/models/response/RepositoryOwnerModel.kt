package com.example.github.models.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RepositoryOwnerModel(
    val login: String?,
    @SerializedName("avatar_url")
    val avatar: String?,
) : Serializable