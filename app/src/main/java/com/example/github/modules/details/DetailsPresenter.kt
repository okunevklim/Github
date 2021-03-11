package com.example.github.modules.details

import android.annotation.SuppressLint
import com.example.github.models.response.RepositoryInfoAsString
import com.example.github.models.response.RepositoryModel
import com.example.github.network.Api
import com.example.github.viewmodels.RepositoriesViewModel
import com.example.github.viewmodels.RoomViewModel
import com.example.vk.modules.base.Presenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DetailsPresenter internal constructor() : Presenter<IDetailsView?>(), IDetailsPresenter {

    override fun onViewAttached() {}
    override fun onViewDetached() {}

    @SuppressLint("CheckResult")
    override fun insertRepository(
        repositoryModel: RepositoryModel,
        roomViewModel: RoomViewModel,
        repositoriesViewModel: RepositoriesViewModel
    ) {
        val repositoryAsString =
            RepositoryInfoAsString(repositoryModel.id, Api.gson.toJson(repositoryModel))
        roomViewModel.insertRepository(repositoryAsString)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                view?.handleIsInserting(1)
            }
    }

    @SuppressLint("CheckResult")
    override fun deleteRepository(
        repositoryModel: RepositoryModel,
        roomViewModel: RoomViewModel,
        repositoriesViewModel: RepositoriesViewModel
    ) {
        val repositoryAsString =
            RepositoryInfoAsString(repositoryModel.id, Api.gson.toJson(repositoryModel))
        roomViewModel.deleteRepository(repositoryAsString)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                view?.handleIsInserting(0)
            }
    }

    @SuppressLint("CheckResult")
    override fun loadPostsFromDatabase(
        roomViewModel: RoomViewModel,
        repositoryModel: RepositoryModel,
        repositoriesViewModel: RepositoriesViewModel
    ) {
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
                if (repositories.contains(repositoryModel)) {
                    deleteRepository(repositoryModel, roomViewModel, repositoriesViewModel)
                } else {
                    insertRepository(repositoryModel, roomViewModel, repositoriesViewModel)
                }
            }
            ) {
                view?.showErrorSnackBar()
            }

    }


    @SuppressLint("CheckResult")
    override fun isFromDatabase(
        roomViewModel: RoomViewModel,
        repositoryModel: RepositoryModel,
        repositoriesViewModel: RepositoriesViewModel
    ) {
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
                if (repositories.contains(repositoryModel)) {
                    repositoriesViewModel.handleIsInserting(1)
                } else repositoriesViewModel.handleIsInserting(0)
            }) {
                view?.showErrorSnackBar()
            }


    }
}