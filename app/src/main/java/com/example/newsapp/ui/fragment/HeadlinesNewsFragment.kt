package com.example.newsapp.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.newsapp.R
import com.example.newsapp.adapter.CategoryViewPagerAdapter
import com.example.newsapp.databinding.FragmentHeadlinesNewsBinding
import com.google.android.material.tabs.TabLayoutMediator

class HeadlinesNewsFragment : Fragment(R.layout.fragment_headlines_news) {
    private var fragmentBinding: FragmentHeadlinesNewsBinding? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHeadlinesNewsBinding.bind(view)
        fragmentBinding = binding
        val articleCategory = resources.getStringArray(R.array.articles_category)
        val articleTabAdapter = CategoryViewPagerAdapter(this)

        binding.articleViewPage2.adapter = articleTabAdapter
        articleTabAdapter.setData(articleCategory.toList())

        with(binding) {
            val tabLayoutMediator = TabLayoutMediator(tabLayout, articleViewPage2) { tab, position ->
                tab.text = articleCategory[position]
            }
            tabLayoutMediator.attach()
        }
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }
}