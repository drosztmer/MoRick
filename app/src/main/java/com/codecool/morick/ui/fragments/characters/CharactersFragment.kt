package com.codecool.morick.ui.fragments.characters

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codecool.morick.R
import com.codecool.morick.adapters.CharactersAdapter
import com.codecool.morick.databinding.FragmentCharactersBinding
import com.codecool.morick.util.Constants.Companion.CHARACTERS
import com.codecool.morick.util.NetworkListener
import com.codecool.morick.util.NetworkResult
import com.codecool.morick.util.Util
import com.codecool.morick.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CharactersFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel
    private val mAdapter by lazy { CharactersAdapter(CHARACTERS) }

    private lateinit var networkListener: NetworkListener

    private var loading = false
    private var onSCrollExecuted = false
    private var pageNumber = 1
    private var name = ""
    private var maxPageNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharactersBinding.inflate(inflater, container, false)
        pageNumber = 1
        setupRecyclerView()

        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.adapter = mAdapter

        setHasOptionsMenu(true)

        mainViewModel.readBackOnline.observe(viewLifecycleOwner, {
            mainViewModel.backOnline = it
        })

        lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext()).collect() { status ->
                Log.d("NetworkListener", status.toString())
                mainViewModel.networkStatus = status
                mainViewModel.showNetworkStatus()
                requestApiData("")
            }
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        val mLayoutManager = LinearLayoutManager(requireContext())
        binding.charactersRecyclerView.apply {
            adapter = mAdapter
            layoutManager = mLayoutManager
        }
        binding.charactersRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val visibleItemCount = mLayoutManager.childCount
                    val pastVisibleItem = mLayoutManager.findFirstVisibleItemPosition()
                    val total = mLayoutManager.itemCount

                    if (!loading) {
                        if ((visibleItemCount + pastVisibleItem) >= total && pastVisibleItem >= 0) {
                            requestNextPage(name)
                        }
                    }
                }
            }
        })
        showShimmerEffect()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.characters_menu, menu)

        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem.actionView as SearchView

        searchView.isSubmitButtonEnabled = true

        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            pageNumber = 1
            name = query
            requestApiData(name)
        }
        Util.hideKeyboard(requireActivity())
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    private fun requestApiData(name: String) {
        mainViewModel.getCharacters(name, pageNumber)
        mainViewModel.rickAndMortyResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    val characterResponse = response.data
                    characterResponse?.let {
                        maxPageNumber = it.info.pages
                        mAdapter.setData(it.results)
                    }
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

    private fun requestNextPage(name: String) {
        loading = true
        binding.progressBar.isVisible = true
        pageNumber += 1
        mainViewModel.getNextPage(name, pageNumber)
        mainViewModel.nextPageResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.progressBar.isVisible = false
                    val characterResponse = response.data
                    characterResponse?.let { mAdapter.addToList(it.results) }
                    loading = false
                }
                is NetworkResult.Error -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    loading = false
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        })

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