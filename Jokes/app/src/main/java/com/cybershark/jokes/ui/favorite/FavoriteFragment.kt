package com.cybershark.jokes.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cybershark.jokes.R
import com.cybershark.jokes.data.room.FavoriteJokeDB
import com.cybershark.jokes.data.room.FavoriteJokeDao
import com.cybershark.jokes.data.room.JokeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteFragment : Fragment(R.layout.fragment_favorites) {

    private lateinit var tvEmptyHint: TextView
    private lateinit var favoritesAdapter: FavoritesAdapter
    private lateinit var pbLoading: ProgressBar
    private lateinit var rvFavorites: RecyclerView
    private lateinit var favoriteJokeDao: FavoriteJokeDao

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvFavorites = view.findViewById(R.id.rvFavorites)
        pbLoading = view.findViewById(R.id.pbLoading)
        tvEmptyHint = view.findViewById(R.id.tvEmptyHint)

        setupRecyclerView()
        setupRoom()
    }

    private fun setupRecyclerView() {
        favoritesAdapter = FavoritesAdapter(deleteJokeAction)
        rvFavorites.apply {
            this.adapter = favoritesAdapter
            this.layoutManager = LinearLayoutManager(context)
            this.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            this.itemAnimator = DefaultItemAnimator()
            this.setHasFixedSize(true)
        }
    }

    private val deleteJokeAction: (JokeEntity) -> Unit = { joke ->
        Log.e("favoriteFragment", "delete clicked")
        pbLoading.isVisible = true
        // function that needs to be done when deleting joke of id = jokeId
        lifecycleScope.launch(Dispatchers.IO) {
            favoriteJokeDao.removeJoke(joke)
        }

        pbLoading.isVisible = false
    }

    private fun setupRoom() {
        favoriteJokeDao = FavoriteJokeDB.getDatabase(requireContext()).favoriteJokeDao()
        getListFromRoomAndDisplay()
    }

    private fun getListFromRoomAndDisplay() {
        pbLoading.isVisible = true

        lifecycleScope.launch {
            val favJokesList = withContext(Dispatchers.IO) { favoriteJokeDao.getAllSavedJokes() }
            favoritesAdapter.submitList(favJokesList)
            favoritesAdapter.notifyDataSetChanged()
            tvEmptyHint.isVisible = favJokesList.isEmpty()
        }

        pbLoading.isVisible = false
    }

}