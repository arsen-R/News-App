package com.example.newsapp.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.newsapp.R
import com.example.newsapp.adapter.CategoryViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HeadlinesNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager2: ViewPager2 = view.findViewById(R.id.articleNewsViewPage2)

        val articleCategory = resources.getStringArray(R.array.articles_category)

        val articleTabAdapter = CategoryViewPagerAdapter(this)
        viewPager2.adapter = articleTabAdapter
        articleTabAdapter.setData(articleCategory.toList())

        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)

        val tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = articleCategory[position]
        }
        tabLayoutMediator.attach()
    }
}