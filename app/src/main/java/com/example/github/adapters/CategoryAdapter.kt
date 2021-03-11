package com.example.github.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.github.modules.saved.SavedFragment
import com.example.github.modules.search.SearchFragment

class CategoryAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return COUNT_NUMBER
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SearchFragment()
            1 -> SavedFragment()
            else -> throw IllegalArgumentException("Invalid position=$position")
        }
    }

    companion object {
        const val COUNT_NUMBER = 2
    }
}