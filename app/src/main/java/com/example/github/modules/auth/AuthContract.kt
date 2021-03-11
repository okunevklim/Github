package com.example.github.modules.auth

import android.os.Bundle
import com.example.github.viewmodels.RepositoriesViewModel
import com.example.vk.modules.base.IPresenter
import com.example.vk.modules.base.IView

interface IAuthView : IView<IAuthPresenter?> {
    fun navigateToListFragment()
}

interface IAuthPresenter : IPresenter<IAuthView?> {
    fun getAccessToken(authCode: String, repositoriesViewModel: RepositoriesViewModel)
}