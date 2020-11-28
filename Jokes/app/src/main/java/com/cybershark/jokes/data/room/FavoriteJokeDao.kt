package com.cybershark.jokes.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteJokeDao {

    @Insert
    suspend fun saveJoke(jokeEntity: JokeEntity)

    @Delete
    suspend fun removeJoke(jokeEntity: JokeEntity)

    @Query("select * from jokes")
    fun getAllSavedJokes(): List<JokeEntity>

    @Query("delete from jokes")
    suspend fun deleteAllJokes()

    @Query("select count(*) from jokes where jokeId=:jokeId")
    suspend fun doesJokeExist(jokeId: Int): Int

}