package com.codecool.morick.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.codecool.morick.R
import com.codecool.morick.adapters.CharactersAdapter
import com.codecool.morick.databinding.FragmentCharactersBinding
import com.codecool.morick.util.NetworkListener
import com.codecool.morick.util.NetworkResult
import com.codecool.morick.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CharactersFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentCharactersBinding? = null
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
        _binding = FragmentCharactersBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        setHasOptionsMenu(true)

        setupRecyclerView()

        mainViewModel.readBackOnline.observe(viewLifecycleOwner, {
            mainViewModel.backOnline = it
        })

        lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext()).collect() { status ->
                Log.d("NetworkListener", status.toString())
                mainViewModel.networkStatus = status
                mainViewModel.showNetworkStatus()
                requestApiData()
            }
        }

        return binding.root
    }

    private fun requestApiData() {
        mainViewModel.getCharacters()
        mainViewModel.rickAndMortyResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    val characterResponse = response.data
                    characterResponse?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }

    private fun setupRecyclerView() {
        binding.charactersRecyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        showShimmerEffect()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.characters_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {

        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
    private fun showShimmerEffect() {
        binding.charactersRecyclerView.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.charactersRecyclerView.hideShimmer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}