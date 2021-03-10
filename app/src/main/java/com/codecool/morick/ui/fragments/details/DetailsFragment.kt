package com.codecool.morick.ui.fragments.details

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.codecool.morick.R
import com.codecool.morick.databinding.FragmentDetailsBinding
import com.codecool.morick.util.Constants.Companion.UNKNOWN_LOWERCASE
import com.codecool.morick.util.Util
import com.codecool.morick.viewmodels.MainViewModel

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel

    private val args by navArgs<DetailsFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        mainViewModel.isLocationLoaded.value = false
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val character = args.character
        binding.character = character
        if (character.location.name != UNKNOWN_LOWERCASE) {
            binding.locationCardview.setOnClickListener {
                val locationId = Util.getIdFromUrl(character.location.url)
                val action = DetailsFragmentDirections.actionDetailsFragmentToLocationFragment(locationId)
                findNavController().navigate(action)
            }
        }
        if (character.origin.name != UNKNOWN_LOWERCASE) {
            binding.originCardview.setOnClickListener {
                val locationId = Util.getIdFromUrl(character.origin.url)
                val action = DetailsFragmentDirections.actionDetailsFragmentToLocationFragment(locationId)
                findNavController().navigate(action)
            }
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.details_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.details_favorite -> {
                Toast.makeText(requireContext(), "Favorite!", Toast.LENGTH_SHORT).show()
            }
            R.id.details_share -> {
                Toast.makeText(requireContext(), "Share!", Toast.LENGTH_SHORT).show()
            }
            R.id.details_save -> {
                Toast.makeText(requireContext(), "Save!", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}