package com.cybershark.jokes.ui.home

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.cybershark.jokes.R
import com.cybershark.jokes.data.api.ApiConstants
import com.cybershark.jokes.data.api.JokeApiService
import com.cybershark.jokes.data.models.Joke
import com.cybershark.jokes.data.room.FavoriteJokeDB
import com.cybershark.jokes.data.room.FavoriteJokeDao
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var cardJoke: CardView
    private lateinit var btnGetJoke: Button
    private lateinit var tvJokeSetupHome: TextView
    private lateinit var tvJokePunchlineHome: TextView
    private lateinit var ibFavHome: ImageButton
    private lateinit var pbLoadingHome: ProgressBar
    private lateinit var retrofitClient: Retrofit
    private lateinit var jokeApiService: JokeApiService
    private lateinit var favoriteJokeDao: FavoriteJokeDao

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get views from Id's
        btnGetJoke = view.findViewById(R.id.btnGetJoke)
        cardJoke = view.findViewById(R.id.cardJoke)
        tvJokeSetupHome = view.findViewById(R.id.tvJokeSetupHome)
        tvJokePunchlineHome = view.findViewById(R.id.tvJokePunchlineHome)
        ibFavHome = view.findViewById(R.id.ibFavHome)
        pbLoadingHome = view.findViewById(R.id.pbLoadingHome)

        setupRoom()
        setupRetrofit()
        setupListeners()
    }

    private fun setupRoom() {
        favoriteJokeDao = FavoriteJokeDB.getDatabase(requireContext()).favoriteJokeDao()
    }

    private fun setupRetrofit() {
        retrofitClient = Retrofit.Builder()
            .baseUrl(ApiConstants.apiEndpoint)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        jokeApiService = retrofitClient.create(JokeApiService::class.java)
    }

    private fun setupListeners() {
        btnGetJoke.setOnClickListener { getJokeFromApi() }
    }

    private fun getJokeFromApi() {
        lifecycleScope.launch {
            // show loading bar
            pbLoadingHome.isVisible = true

            // get joke
            try {
                val jokeResponse = jokeApiService.getRandomJoke()
                if (jokeResponse.isSuccessful) {
                    val joke: Joke? = jokeResponse.body()
                    joke?.let {
                        cardJoke.isVisible = true
                        tvJokeSetupHome.text = it.setup
                        tvJokePunchlineHome.text = it.punchline
                        setIfFavorite(it)
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                cardJoke.isVisible = false
            }

            // hide loading bar
            pbLoadingHome.isVisible = false
        }
    }

    private fun setIfFavorite(joke: Joke) {
        lifecycleScope.launch {
            val count = favoriteJokeDao.doesJokeExist(joke.id)
            if (count == 0) {
                // joke not saved
                setIconAsNotSelected()
                setListenerToSaveJoke(joke)
            } else {
                setIconAsSelected()
                setListenerToRemoveJoke(joke)
            }
        }
    }

    private fun setListenerToRemoveJoke(joke: Joke) {
        ibFavHome.setOnClickListener {
            lifecycleScope.launch {
                favoriteJokeDao.removeJoke(joke.getJokeEntity())
                setIconAsNotSelected()
                setListenerToSaveJoke(joke)
            }
        }
    }

    private fun setListenerToSaveJoke(joke: Joke) {
        ibFavHome.setOnClickListener {
            lifecycleScope.launch {
                favoriteJokeDao.saveJoke(joke.getJokeEntity())
                setIconAsSelected()
                setListenerToRemoveJoke(joke)
            }
        }
    }

    private fun setIconAsSelected() {
        ibFavHome.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_favorite_selected
            )
        )
    }

    private fun setIconAsNotSelected() {
        ibFavHome.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_favorite
            )
        )
    }
}