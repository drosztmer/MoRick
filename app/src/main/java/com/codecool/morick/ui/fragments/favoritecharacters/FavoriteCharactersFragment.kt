package com.codecool.morick.ui.fragments.favoritecharacters

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codecool.morick.R
import com.codecool.morick.adapters.CharactersAdapter
import com.codecool.morick.databinding.FragmentFavoriteCharactersBinding
import com.codecool.morick.util.Constants
import com.codecool.morick.util.Constants.Companion.FAVORITES
import com.codecool.morick.viewmodels.MainViewModel

class FavoriteCharactersFragment : Fragment() {

    private var _binding: FragmentFavoriteCharactersBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel
    private val mAdapter by lazy { CharactersAdapter(FAVORITES) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteCharactersBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.adapter = mAdapter

        return binding.root
    }

}