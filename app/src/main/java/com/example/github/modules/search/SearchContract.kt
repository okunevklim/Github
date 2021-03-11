package com.example.github.modules.search

import com.example.github.models.response.RepositoryModel
import com.example.vk.modules.base.IPresenter
import com.example.vk.modules.base.IView

interface ISearchView : IView<ISearchPresenter?> {
    fun performSearch(text: String, isRefresh: Boolean)
    fun handleResults(results: ArrayList<RepositoryModel>, isRefresh: Boolean)
    fun handleEmptyResult()
    fun updateEmptyResult(isVisible: Boolean)
    fun updateProgressBar(isVisible: Boolean)
    fun showRateLimitErrorToast()
}

interface ISearchPresenter : IPresenter<ISearchView?> {
    fun searchRepositories(query: String, pageNumber: Int, isRefresh: Boolean)
}