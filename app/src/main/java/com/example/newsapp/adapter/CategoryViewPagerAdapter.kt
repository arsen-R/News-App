package com.example.newsapp.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.newsapp.view.fragment.HeadlinesItemFragment

class CategoryViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private var data = listOf<String>()

    fun setData(data: List<String>) {
        this.data = data
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = data.size

    override fun createFragment(position: Int): Fragment = HeadlinesItemFragment.newInstance(data[position])
}