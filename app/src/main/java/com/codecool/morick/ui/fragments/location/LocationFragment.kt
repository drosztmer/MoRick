package com.codecool.morick.ui.fragments.location

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.codecool.morick.adapters.PagerAdapter
import com.codecool.morick.databinding.FragmentLocationBinding
import com.codecool.morick.util.Constants.Companion.LOCATION_BUNDLE
import com.codecool.morick.util.Constants.Companion.LOCATION_CHARACTERS_TITLE
import com.codecool.morick.util.Constants.Companion.LOCATION_INFO_TITLE
import com.codecool.morick.util.NetworkListener
import com.codecool.morick.util.NetworkResult
import com.codecool.morick.viewmodels.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LocationFragment : Fragment() {

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<LocationFragmentArgs>()

    private lateinit var mainViewModel: MainViewModel
    private lateinit var networkListener: NetworkListener
    private var resultBundle: Bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        mainViewModel.isLocationLoaded.value = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        val locationId = args.locationId

        mainViewModel.readBackOnline.observe(viewLifecycleOwner, {
            mainViewModel.backOnline = it
        })

        lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext()).collect() { status ->
                Log.d("NetworkListener", status.toString())
                mainViewModel.networkStatus = status
                mainViewModel.showNetworkStatus()
                requestApiData(locationId)
            }
        }

        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        return binding.root
    }

    private fun requestApiData(locationId: String) {
        mainViewModel.getLocationById(locationId)
        mainViewModel.locationResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.progressBar.isVisible = false
                    mainViewModel.isLocationLoaded.value = true
                    val locationResponse = response.data
                    locationResponse?.let { resultBundle.putParcelable(LOCATION_BUNDLE, it) }

                    setupViewPagerWithFragments(resultBundle)

                }
                is NetworkResult.Error -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        })
    }

    private fun setupViewPagerWithFragments(resultBundle: Bundle) {
        val fragments = ArrayList<Fragment>()
        fragments.add(LocationInfoFragment())
        fragments.add(LocationCharactersFragment())

        val titles = ArrayList<String>()
        titles.add(LOCATION_INFO_TITLE)
        titles.add(LOCATION_CHARACTERS_TITLE)

        val pagerAdapter = PagerAdapter(
            resultBundle, fragments, childFragmentManager, viewLifecycleOwner.lifecycle
        )

        binding.viewpager2.apply {
            adapter = pagerAdapter
        }

        TabLayoutMediator(binding.tablayout, binding.viewpager2) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}