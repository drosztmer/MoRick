package com.codecool.morick.ui.fragments.favoritecharacters

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.codecool.morick.R
import com.codecool.morick.adapters.CharactersAdapter
import com.codecool.morick.databinding.FragmentFavoriteCharactersBinding
import com.codecool.morick.util.Constants.Companion.FAVORITES
import com.codecool.morick.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteCharactersFragment : Fragment() {

    private var _binding: FragmentFavoriteCharactersBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()
    private val mAdapter by lazy { CharactersAdapter(FAVORITES) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteCharactersBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.adapter = mAdapter

        setHasOptionsMenu(true)

        setupRecyclerView()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_characters_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_all_favorite_characters_menu) {
            confirmDeleteAll()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmDeleteAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            // mainViewModel.deleteAllFavorites()
            showSnackBar()
        }
        builder.setNegativeButton(getString(R.string.no)) { _, _ -> }
        builder.setTitle(getString(R.string.alert_dialog_title))
        builder.setMessage(getString(R.string.alert_dialog_message))
        builder.create().show()
    }

    private fun showSnackBar() {
        Snackbar.make(binding.root, getString(R.string.successfully_deleted_everything), Snackbar.LENGTH_SHORT).setAction("Okay") {}.show()
    }

    private fun setupRecyclerView() {
        binding.favoriteCharactersRecyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}