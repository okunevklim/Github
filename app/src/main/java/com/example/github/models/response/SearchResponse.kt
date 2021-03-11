package com.example.github.models.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SearchResponse(
    @SerializedName("total_count")
    val totalCount: Int?,
    val items: ArrayList<RepositoryModel>?
 ) : Serializable