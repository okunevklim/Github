package com.example.github.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.github.databinding.LayoutItemRepositoryBinding
import com.example.github.interfaces.OnRepositoryClickListener
import com.example.github.models.response.RepositoryModel


class SearchAdapter(
    private val listener: OnRepositoryClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var results = arrayListOf<RepositoryModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ResultViewHolder(
            LayoutItemRepositoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return results.size
    }

    fun handleResults(results: ArrayList<RepositoryModel>, isRefresh: Boolean) {
        if (isRefresh) {
            this.results.clear()
        }
        this.results.addAll(results)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        (viewHolder as ResultViewHolder).bind(
            results[position],
            listener
        )
    }

    class ResultViewHolder(private val binding: LayoutItemRepositoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            repositoryModel: RepositoryModel,
            listener: OnRepositoryClickListener
        ) {
            binding.repoName.text = repositoryModel.fullName
            binding.repoDescription.text = repositoryModel.description
            binding.root.setOnClickListener {
                listener.onRepositoryClick(repositoryModel)
            }
        }
    }

}