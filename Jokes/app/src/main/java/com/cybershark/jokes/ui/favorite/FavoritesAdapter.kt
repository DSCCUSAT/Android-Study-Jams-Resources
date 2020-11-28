package com.cybershark.jokes.ui.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cybershark.jokes.R
import com.cybershark.jokes.data.room.JokeEntity

class FavoritesAdapter(
    private val removeAction: (JokeEntity) -> Unit
) :
    RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    private var list = emptyList<JokeEntity>()

    inner class FavoritesViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val tvJokeSetup = itemView.findViewById<TextView>(R.id.tvJokeSetup)!!
        private val tvJokePunchline = itemView.findViewById<TextView>(R.id.tvJokePunchline)!!
        private val ibFav = itemView.findViewById<ImageButton>(R.id.ibFav)!!

        fun bindData(jokeEntity: JokeEntity) {
            tvJokeSetup.text = jokeEntity.jokeSetup
            tvJokePunchline.text = jokeEntity.jokePunchline
            ibFav.setOnClickListener {
                // Lambda from fragment to remove joke of given id from room
                removeAction(jokeEntity)
                // notify adapter of change
                list = list.filter { it.jokeId!=jokeEntity.jokeId }
                notifyItemRemoved(adapterPosition)
                notifyItemRangeChanged(adapterPosition,list.size)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        return FavoritesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.joke_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    fun submitList(list: List<JokeEntity>) {
        this.list = list
    }
}