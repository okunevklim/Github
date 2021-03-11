package com.example.github.modules.saved

import android.annotation.SuppressLint
import com.example.github.models.response.RepositoryModel
import com.example.github.network.Api
import com.example.github.viewmodels.RepositoriesViewModel
import com.example.github.viewmodels.RoomViewModel
import com.example.vk.modules.base.Presenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SavedPresenter internal constructor() : Presenter<ISavedView?>(), ISavedPresenter {

    override fun onViewAttached() {}
    override fun onViewDetached() {}

    @SuppressLint("CheckResult")
    override fun checkDatabase(roomViewModel: RoomViewModel) {
        roomViewModel.getCountRepositories()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it == 0) {
                    view?.handleClearButton(false)
                    view?.updateSearchField(false)
                    view?.updateErrorText(true)
                    view?.hideRecycler()
                } else {
                    view?.handleClearButton(true)
                    view?.showRecycler()
                    view?.updateSearchField(true)
                    view?.updateErrorText(false)
                    loadPostsFromDatabase(roomViewModel, false)
                }
            }

    }

    @SuppressLint("CheckResult")
    override fun clearDatabase(
        roomViewModel: RoomViewModel,
        repositoriesViewModel: RepositoriesViewModel
    ) {
        roomViewModel.clearRepositoriesDb()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                view?.showSuccessfulSnackBar()
                repositoriesViewModel.handleIsCleared(1)
            }
    }

    @SuppressLint("CheckResult")
    override fun loadPostsFromDatabase(roomViewModel: RoomViewModel, searchMode: Boolean) {
        roomViewModel.loadAllRepositories()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ listRepositories ->
                val repositories =
                    listRepositories.map {
                        Api.gson.fromJson(
                            it.content,
                            RepositoryModel::class.java
                        )
                    }
                val savedRepositories = arrayListOf<RepositoryModel>()
                savedRepositories.addAll(repositories)
                if (searchMode) {
                    view?.updateSavedRepositories(savedRepositories)
                } else {
                    view?.handleRepositories(savedRepositories)
                }
            }) {
                view?.showSnackBar()
            }
    }
}