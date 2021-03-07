package com.codecool.morick.ui.fragments.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codecool.morick.adapters.PagerAdapter
import com.codecool.morick.databinding.FragmentLocationBinding
import com.google.android.material.tabs.TabLayoutMediator

class LocationFragment : Fragment() {

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)

        val fragments = ArrayList<Fragment>()
        fragments.add(LocationInfoFragment())
        fragments.add(LocationCharactersFragment())

        val titles = ArrayList<String>()
        titles.add("Info")
        titles.add("Characters")

        val resultBundle = Bundle()

        val pagerAdapter = PagerAdapter(
            resultBundle, fragments, requireActivity()
        )

        binding.viewpager2.apply {
            adapter = pagerAdapter
        }

        TabLayoutMediator(binding.tablayout, binding.viewpager2) { tab, position ->
            tab.text = titles[position]
        }.attach()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}