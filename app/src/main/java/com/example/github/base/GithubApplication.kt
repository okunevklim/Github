package com.example.github.base

import android.app.Application
import android.content.Context

class GithubApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this.applicationContext
    }
    companion object {
        lateinit var context: Context
    }
}