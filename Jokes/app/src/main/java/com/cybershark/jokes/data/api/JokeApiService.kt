package com.cybershark.jokes.data.api

import com.cybershark.jokes.data.models.Joke
import retrofit2.Response
import retrofit2.http.GET

interface JokeApiService {

    @GET("jokes/random")
    suspend fun getRandomJoke(): Response<Joke>

}