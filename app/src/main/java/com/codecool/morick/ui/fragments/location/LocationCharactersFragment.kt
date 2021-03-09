package com.codecool.morick.ui.fragments.location

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.codecool.morick.R
import com.codecool.morick.adapters.CharactersAdapter
import com.codecool.morick.databinding.FragmentLocationCharactersBinding
import com.codecool.morick.models.RickAndMortyLocation
import com.codecool.morick.util.Constants
import com.codecool.morick.util.NetworkListener
import com.codecool.morick.util.Util
import com.codecool.morick.viewmodels.MainViewModel

class LocationCharactersFragment : Fragment() {

    private var _binding: FragmentLocationCharactersBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel
    private val mAdapter by lazy { CharactersAdapter() }

    private lateinit var networkListener: NetworkListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLocationCharactersBinding.inflate(inflater, container, false)
        setupRecyclerView()

        val args = arguments
        val location: RickAndMortyLocation? = args?.getParcelable(Constants.LOCATION_BUNDLE)
        val locationIdQuery = location?.let { Util.getIdQueryFromUrls(it.residents) }

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.locationCharactersRecyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        showShimmerEffect()
    }

    private fun showShimmerEffect() {
        binding.locationCharactersRecyclerView.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.locationCharactersRecyclerView.hideShimmer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}