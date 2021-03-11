package com.example.github.modules.search

import android.annotation.SuppressLint
import android.util.Log
import com.example.github.models.response.RepositoryModel
import com.example.github.models.response.SearchResponse
import com.example.github.network.Api
import com.example.github.utils.Constants.ORDER_BY
import com.example.github.utils.Constants.PAGE_SIZE
import com.example.github.utils.Constants.SORTED_BY
import com.example.github.viewmodels.RoomViewModel
import com.example.vk.modules.base.Presenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchPresenter internal constructor() : Presenter<ISearchView?>(), ISearchPresenter {
    override fun onViewAttached() {}
    override fun onViewDetached() {}

    @SuppressLint("CheckResult")
    override fun searchRepositories(query: String, pageNumber: Int, isRefresh: Boolean) {
        view?.updateEmptyResult(false)
        view?.updateProgressBar(true)
        Api.getService().searchRepositories(
            query,
            SORTED_BY,
            ORDER_BY,
            PAGE_SIZE,
            pageNumber
        )
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { searchResponse: SearchResponse?, _: Throwable? ->
                if (searchResponse != null) {
                    val totalNumberOfPages = searchResponse.totalCount?.toDouble()?.div(30) ?: 0.00
                    if (totalNumberOfPages == 0.00) {
                        view?.handleEmptyResult()
                        view?.updateEmptyResult(true)
                    } else {
                        if (!searchResponse.items.isNullOrEmpty()) {
                            view?.handleResults(searchResponse.items, isRefresh)
                        }
                    }
                } else {
                    if (isRefresh) {
                        view?.handleEmptyResult()
                    }
                    view?.showRateLimitErrorToast()
                }
                view?.updateProgressBar(false)
            }
    }
}