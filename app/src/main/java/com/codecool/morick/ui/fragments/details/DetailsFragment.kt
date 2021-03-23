package com.codecool.morick.ui.fragments.details

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DetailsFragmentArgs>()
    private val mainViewModel: MainViewModel by viewModels()

    private var characterSavedToFavorites = false
    private var savedToFavoritesCharacterId = 0

    private lateinit var menuItem: MenuItem
    private lateinit var character: RickAndMortyCharacter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        mainViewModel.readFavoriteCharacters.observe(
            viewLifecycleOwner,
            { favoriteCharacterEntity ->
                try {
                    for (savedToFavoriteCharacter in favoriteCharacterEntity) {
                        Log.d("FAVORITES: ", savedToFavoriteCharacter.character.name)
                        if (savedToFavoriteCharacter.character.id == args.character.id) {
                            changeMenuItemColor(menuItem, R.color.yellow_dark)
                            savedToFavoritesCharacterId = savedToFavoriteCharacter.id
                            characterSavedToFavorites = true
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
                putExtra(
                    Intent.EXTRA_TEXT, Util.createStringMessageFromCharacter(
                        requireContext(),
                        character
                    )
                )
                type = "text/plain"
            }
            startActivity(shareIntent)
        } else if (item.itemId == R.id.details_save) {
            val bitmap: Bitmap = (binding.detailImage.drawable as BitmapDrawable).bitmap

            saveImage(bitmap)
//            Toast.makeText(requireContext(), "Save!", Toast.LENGTH_SHORT).show()
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
        val favoriteCharacterEntity = FavoriteCharacterEntity(
            savedToFavoritesCharacterId,
            args.character
        )
        mainViewModel.deleteFavoriteCharacter(favoriteCharacterEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar(getString(R.string.character_removed_from_favorites))
        characterSavedToFavorites = false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.detailsLayout, message, Snackbar.LENGTH_SHORT)
            .setAction(getString(R.string.okay)) {}.show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(requireContext(), color))
    }

    private fun saveImage(bitmap: Bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    100
                )
            } else {
                saveImageToStorage(bitmap)
            }
        } else {
            saveImageToStorage(bitmap)
        }
    }

    private fun saveImageToStorage(bitmap: Bitmap) {
        val fos: OutputStream?
        val name = System.currentTimeMillis().toString()
        try {
            fos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver: ContentResolver = requireContext().contentResolver
                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name + ".jpg")
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                resolver.openOutputStream(imageUri!!)
            } else {
                val imagesDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        .toString()
                val image = File(imagesDir, name + ".jpg")
                FileOutputStream(image)
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos?.close()
            Toast.makeText(
                requireContext(),
                "Image saved successfully",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(
                    requireContext(),
                    "Permission not granted, image can't be saved",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        changeMenuItemColor(menuItem, R.color.white)
        _binding = null
    }

}