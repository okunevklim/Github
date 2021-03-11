package com.example.github.modules.details

import com.example.github.models.response.RepositoryModel
import com.example.github.viewmodels.RepositoriesViewModel
import com.example.github.viewmodels.RoomViewModel
import com.example.vk.modules.base.IPresenter
import com.example.vk.modules.base.IView

interface IDetailsView : IView<IDetailsPresenter?> {
    fun handleIsInserting(value: Int)
    fun showSnackBar()
    fun initViewModel()
    fun showErrorSnackBar()
}

interface IDetailsPresenter : IPresenter<IDetailsView?> {
    fun insertRepository(
        repositoryModel: RepositoryModel, roomViewModel: RoomViewModel,
        repositoriesViewModel: RepositoriesViewModel
    )

    fun deleteRepository(
        repositoryModel: RepositoryModel, roomViewModel: RoomViewModel,
        repositoriesViewModel: RepositoriesViewModel
    )

    fun loadPostsFromDatabase(
        roomViewModel: RoomViewModel, repositoryModel: RepositoryModel,
        repositoriesViewModel: RepositoriesViewModel
    )

    fun isFromDatabase(
        roomViewModel: RoomViewModel,
        repositoryModel: RepositoryModel,
        repositoriesViewModel: RepositoriesViewModel
    )
}
