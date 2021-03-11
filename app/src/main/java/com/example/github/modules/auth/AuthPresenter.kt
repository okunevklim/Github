package com.example.github.modules.auth

import android.annotation.SuppressLint
import com.example.github.models.request.TokenRequest
import com.example.github.models.response.AccessTokenResponse
import com.example.github.network.Api
import com.example.github.utils.Constants.AUTHORIZED
import com.example.github.utils.Constants.CLIENT_ID
import com.example.github.utils.Constants.CLIENT_SECRET
import com.example.github.utils.Constants.NOT_AUTHORIZED
import com.example.github.utils.Constants.REDIRECT_URI
import com.example.github.utils.PreferencesHelper
import com.example.github.viewmodels.RepositoriesViewModel
import com.example.vk.modules.base.Presenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AuthPresenter internal constructor() : Presenter<IAuthView?>(), IAuthPresenter {

    override fun onViewAttached() {}
    override fun onViewDetached() {}

    @SuppressLint("CheckResult")
    override fun getAccessToken(authCode: String, repositoriesViewModel: RepositoriesViewModel) {
        Api.getService().getAccessToken(
            TokenRequest(
                CLIENT_ID,
                CLIENT_SECRET,
                authCode,
                REDIRECT_URI
            )
        )
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { githubResponse: AccessTokenResponse?, _: Throwable? ->
                if (githubResponse != null) {
                    if (!githubResponse.accessToken.isNullOrEmpty()) {
                        PreferencesHelper.putAccessToken(githubResponse.accessToken)
                        repositoriesViewModel.handleIsAuthorized(AUTHORIZED)
                    } else {
                        repositoriesViewModel.handleIsAuthorized(NOT_AUTHORIZED)
                    }
                    view?.navigateToListFragment()
                } else {
                    repositoriesViewModel.handleIsAuthorized(NOT_AUTHORIZED)
                    view?.navigateToListFragment()
                }
            }
    }
}