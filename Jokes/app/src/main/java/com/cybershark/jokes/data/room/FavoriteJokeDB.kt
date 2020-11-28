package com.cybershark.jokes.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [JokeEntity::class], version = 1)
abstract class FavoriteJokeDB : RoomDatabase() {

    abstract fun favoriteJokeDao(): FavoriteJokeDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: FavoriteJokeDB? = null

        fun getDatabase(context: Context): FavoriteJokeDB {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteJokeDB::class.java,
                    "jokes_db"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}