package com.example.github.models.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AccessTokenResponse(
    @SerializedName("access_token")
    val accessToken: String?,
    @SerializedName("scope")
    val scope: String?,
    @SerializedName("token_type")
    val tokenType: String?,
) : Serializable
