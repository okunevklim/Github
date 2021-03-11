package com.example.github.modules.saved

import com.example.github.models.response.RepositoryModel
import com.example.github.viewmodels.RepositoriesViewModel
import com.example.github.viewmodels.RoomViewModel
import com.example.vk.modules.base.IPresenter
import com.example.vk.modules.base.IView

interface ISavedView : IView<ISavedPresenter?> {
    fun setupRecycler()
    fun updateSearchField(isShow: Boolean)
    fun updateErrorText(isShow: Boolean)
    fun handleRepositories(repositories: ArrayList<RepositoryModel>)
    fun showSnackBar()
    fun hideRecycler()
    fun showRecycler()
    fun performSearch(text: CharSequence?)
    fun updateSavedRepositories(repositories: ArrayList<RepositoryModel>)
    fun handleClearButton(isShow: Boolean)
    fun showSuccessfulSnackBar()
    fun initViewModels()
    fun handleEmptyResult(isVisible: Boolean)
}

interface ISavedPresenter : IPresenter<ISavedView?> {
    fun checkDatabase(roomViewModel: RoomViewModel)
    fun loadPostsFromDatabase(roomViewModel: RoomViewModel, searchMode: Boolean)
    fun clearDatabase(roomViewModel: RoomViewModel, repositoriesViewModel: RepositoriesViewModel)
}