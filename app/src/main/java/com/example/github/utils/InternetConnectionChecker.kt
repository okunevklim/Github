package com.example.github.utils

import androidx.lifecycle.LifecycleOwner
import com.example.github.base.GithubApplication
import com.example.github.network.NetworkConnection

object InternetConnectionChecker {
    fun checkInternetConnection(
        viewLifecycleOwner: LifecycleOwner,
        listener: (isConnected: Boolean) -> Unit
    ) {
        val networkConnection = NetworkConnection(GithubApplication.context)
        networkConnection.observe(viewLifecycleOwner, { isConnected ->
            listener.invoke(isConnected)
        }
        )
    }
}