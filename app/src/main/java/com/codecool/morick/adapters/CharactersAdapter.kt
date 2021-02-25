package com.codecool.morick.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.codecool.morick.databinding.ItemCharacterBinding
import com.codecool.morick.models.RickAndMortyCharacter
import com.codecool.morick.models.RickAndMortyResponse
import com.codecool.morick.util.CharactersDiffUtil

class CharactersAdapter: RecyclerView.Adapter<CharactersAdapter.MyViewHolder>() {

    private var characters = emptyList<RickAndMortyCharacter>()

    class MyViewHolder(private val binding: ItemCharacterBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(character: RickAndMortyCharacter) {
            binding.character = character
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCharacterBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentCharacter = characters[position]
        holder.bind(currentCharacter)
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    fun setData(newResponse: RickAndMortyResponse) {
        val charactersDiffUtil = CharactersDiffUtil(characters, newResponse.results)
        val diffUtilResult = DiffUtil.calculateDiff(charactersDiffUtil)
        characters = newResponse.results
        diffUtilResult.dispatchUpdatesTo(this)
    }
}