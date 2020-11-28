package com.cybershark.jokes.data.models

import com.cybershark.jokes.data.room.JokeEntity
import com.google.gson.annotations.SerializedName

data class Joke(
    @SerializedName("id")
    val id: Int,
    @SerializedName("punchline")
    val punchline: String,
    @SerializedName("setup")
    val setup: String,
    @SerializedName("type")
    val type: String
) {
    fun getJokeEntity(): JokeEntity {
        return JokeEntity(id, punchline, setup, type)
    }
}