package com.codecool.morick.ui.fragments.location

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codecool.morick.databinding.FragmentLocationInfoBinding
import com.codecool.morick.models.RickAndMortyLocation
import com.codecool.morick.util.Constants.Companion.LOCATION_BUNDLE

class LocationInfoFragment : Fragment() {

    private var _binding: FragmentLocationInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLocationInfoBinding.inflate(inflater, container, false)

        val args = arguments
        val location: RickAndMortyLocation? = args?.getParcelable(LOCATION_BUNDLE)

        binding.location = location

        return binding.root
    }

}