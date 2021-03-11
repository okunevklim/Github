package com.example.github.interfaces

import com.example.github.models.response.RepositoryModel

interface OnRepositoryClickListener {
    fun onRepositoryClick(repositoryModel: RepositoryModel)
}