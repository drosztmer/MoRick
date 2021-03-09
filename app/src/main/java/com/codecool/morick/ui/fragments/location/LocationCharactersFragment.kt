package com.codecool.morick.ui.fragments.location

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.codecool.morick.R
import com.codecool.morick.adapters.CharactersAdapter
import com.codecool.morick.databinding.FragmentLocationCharactersBinding
import com.codecool.morick.models.RickAndMortyLocation
import com.codecool.morick.util.Constants
import com.codecool.morick.util.Constants.Companion.LOCATION
import com.codecool.morick.util.NetworkListener
import com.codecool.morick.util.NetworkResult
import com.codecool.morick.util.Util
import com.codecool.morick.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LocationCharactersFragment : Fragment() {

    private var _binding: FragmentLocationCharactersBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel
    private val mAdapter by lazy { CharactersAdapter(LOCATION) }

    private lateinit var networkListener: NetworkListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("LOCATIONCHARACTERSSSSSSS", "RE")

        // Inflate the layout for this fragment
        _binding = FragmentLocationCharactersBinding.inflate(inflater, container, false)
        setupRecyclerView()

        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.adapter = mAdapter

        val args = arguments
        val location: RickAndMortyLocation? = args?.getParcelable(Constants.LOCATION_BUNDLE)
        val locationIdQuery = if (location != null) {
            Util.getIdQueryFromUrls(location.residents)
        } else {
            ""
        }

        requestApiData(locationIdQuery)

        return binding.root
    }

    private fun requestApiData(idList: String) {
        mainViewModel.getMultipleCharacters(idList)
        mainViewModel.multipleCharacters.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    val multipleCharacters = response.data
                    multipleCharacters?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
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