package com.codecool.morick.ui.fragments.location

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codecool.morick.R
import com.codecool.morick.databinding.FragmentLocationCharactersBinding
import com.codecool.morick.models.RickAndMortyLocation
import com.codecool.morick.util.Constants
import com.codecool.morick.util.Util

class LocationCharactersFragment : Fragment() {

    private var _binding: FragmentLocationCharactersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLocationCharactersBinding.inflate(inflater, container, false)

        val args = arguments
        val location: RickAndMortyLocation? = args?.getParcelable(Constants.LOCATION_BUNDLE)
        val locationIdQuery = location?.let { Util.getIdQueryFromUrls(it.residents) }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}