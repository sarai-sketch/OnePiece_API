package com.practica_api.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        FavoritoEntity::class,
        CharacterCacheEntity::class,
        FruitCacheEntity::class,
        SagaCacheEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class OnePieceDatabase : RoomDatabase() {

    abstract fun favoritoDao(): FavoritoDao
    abstract fun characterCacheDao(): CharacterCacheDao
    abstract fun fruitCacheDao(): FruitCacheDao
    abstract fun sagaCacheDao(): SagaCacheDao

    companion object {
        @Volatile
        private var instancia: OnePieceDatabase? = null

        fun obtener(context: Context): OnePieceDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    OnePieceDatabase::class.java,
                    "onepiece.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instancia = it }
            }
    }
}
