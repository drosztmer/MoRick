package com.codecool.morick.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.codecool.morick.data.database.entities.FavoriteCharacterEntity
import com.codecool.morick.databinding.ItemCharacterBinding
import com.codecool.morick.util.CharactersDiffUtil

class FavoriteCharactersAdapter(val from: String): RecyclerView.Adapter<FavoriteCharactersAdapter.MyViewHolder>() {

    var characterEntities = listOf<FavoriteCharacterEntity>()

    class MyViewHolder(private val binding: ItemCharacterBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(favoriteCharacterEntity: FavoriteCharacterEntity, from: String) {
            binding.character = favoriteCharacterEntity.character
            binding.from = from
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCharacterBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentCharacterEntity = characterEntities[position]
        holder.bind(currentCharacterEntity, from)
    }

    override fun getItemCount(): Int {
        return characterEntities.size
    }

    fun setData(newList: List<FavoriteCharacterEntity>) {
        val charactersDiffUtil = CharactersDiffUtil(characterEntities, newList)
        val diffUtilResult = DiffUtil.calculateDiff(charactersDiffUtil)
        characterEntities = newList
        diffUtilResult.dispatchUpdatesTo(this)
    }
}