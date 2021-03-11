package com.example.github.models.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TokenRequest(
    @SerializedName("client_id")
    val clientID: String,
    @SerializedName("client_secret")
    val clientSecret: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("redirect_uri")
    val redirectURI: String
) : Serializable
