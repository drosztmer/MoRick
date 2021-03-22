package com.codecool.morick.ui.fragments.details

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.codecool.morick.R
import com.codecool.morick.data.database.entities.FavoriteCharacterEntity
import com.codecool.morick.databinding.FragmentDetailsBinding
import com.codecool.morick.models.RickAndMortyCharacter
import com.codecool.morick.util.Constants.Companion.UNKNOWN_LOWERCASE
import com.codecool.morick.util.Util
import com.codecool.morick.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DetailsFragmentArgs>()
    private lateinit var mainViewModel: MainViewModel

    private var characterSavedToFavorites = false
    private var savedToFavoritesCharacterId = 0

    private lateinit var menuItem: MenuItem
    private lateinit var character: RickAndMortyCharacter

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
        character = args.character
        binding.character = character
        if (character.location.name != UNKNOWN_LOWERCASE) {
            binding.locationCardview.setOnClickListener {
                val locationId = Util.getIdFromUrl(character.location.url)
                val action =
                    DetailsFragmentDirections.actionDetailsFragmentToLocationFragment(locationId)
                findNavController().navigate(action)
            }
        }
        if (character.origin.name != UNKNOWN_LOWERCASE) {
            binding.originCardview.setOnClickListener {
                val locationId = Util.getIdFromUrl(character.origin.url)
                val action =
                    DetailsFragmentDirections.actionDetailsFragmentToLocationFragment(locationId)
                findNavController().navigate(action)
            }
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.details_menu, menu)
        menuItem = menu.findItem(R.id.details_favorite)
        checkSavedToFavoritesCharacters(menuItem)
    }

    private fun checkSavedToFavoritesCharacters(menuItem: MenuItem) {
        mainViewModel.readFavoriteCharacters.observe(viewLifecycleOwner, { favoriteCharacterEntity ->
            try {
                for (savedToFavoriteCharacter in favoriteCharacterEntity) {
                    Log.d("FAVORITES: ", savedToFavoriteCharacter.character.name)
                    if (savedToFavoriteCharacter.character.id == args.character.id) {
                        changeMenuItemColor(menuItem, R.color.yellow_dark)
                        savedToFavoritesCharacterId = savedToFavoriteCharacter.id
                        characterSavedToFavorites = true
                    } else {
                        changeMenuItemColor(menuItem, R.color.white)
                        savedToFavoritesCharacterId = 0
                        characterSavedToFavorites = false
                    }
                }
            } catch (e: Exception) {
                Log.d("DetailsFragment", e.message.toString())
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.details_favorite && !characterSavedToFavorites) {
            addToFavorites(item)
        } else if (item.itemId == R.id.details_favorite && characterSavedToFavorites) {
            removeFromFavorites(item)
        } else if (item.itemId == R.id.details_share) {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, Util.createStringMessageFromCharacter(requireContext(), character))
                type = "text/plain"
            }
            startActivity(shareIntent)
        } else if (item.itemId == R.id.details_save) {
            Toast.makeText(requireContext(), "Save!", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addToFavorites(item: MenuItem) {
        val favoriteCharacterEntity = FavoriteCharacterEntity(0, args.character)
        mainViewModel.insertFavoriteCharacter(favoriteCharacterEntity)
        changeMenuItemColor(item, R.color.yellow_dark)
        showSnackBar(getString(R.string.character_added_to_favorites))
        characterSavedToFavorites = true
    }

    private fun removeFromFavorites(item: MenuItem) {
        val favoriteCharacterEntity = FavoriteCharacterEntity(savedToFavoritesCharacterId, args.character)
        mainViewModel.deleteFavoriteCharacter(favoriteCharacterEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar(getString(R.string.character_removed_from_favorites))
        characterSavedToFavorites = false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.detailsLayout, message, Snackbar.LENGTH_SHORT).setAction(getString(R.string.okay)) {}.show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(requireContext(), color))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        changeMenuItemColor(menuItem, R.color.white)
        _binding = null
    }

}