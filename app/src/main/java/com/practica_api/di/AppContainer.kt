package com.practica_api.di

import android.content.Context
import com.practica_api.data.local.OnePieceDatabase
import com.practica_api.data.remote.client.RetrofitClient
import com.practica_api.data.repository.BattleCloudRepository
import com.practica_api.data.repository.FavoritosRepository
import com.practica_api.data.repository.NewtonRepository
import com.practica_api.data.repository.OnePieceRepository
import com.practica_api.data.repository.WealthRepository

/** Contenedor de dependencias manual: arma los repositorios con RetrofitClient y Room. */
object AppContainer {

    @Volatile
    private var database: OnePieceDatabase? = null

    fun init(context: Context) {
        if (database == null) {
            synchronized(this) {
                if (database == null) {
                    database = OnePieceDatabase.obtener(context.applicationContext)
                }
            }
        }
    }

    private fun requireDb(context: Context): OnePieceDatabase {
        init(context)
        return database!!
    }

    val onePieceRepository: OnePieceRepository by lazy {
        val db = database
        OnePieceRepository(
            service = RetrofitClient.onePieceService,
            characterCacheDao = db?.characterCacheDao(),
            fruitCacheDao = db?.fruitCacheDao(),
            sagaCacheDao = db?.sagaCacheDao()
        )
    }

    fun getFavoritosRepository(context: Context): FavoritosRepository {
        return FavoritosRepository(requireDb(context).favoritoDao())
    }

    val battleCloudRepository: BattleCloudRepository by lazy {
        BattleCloudRepository(RetrofitClient.sqliteCloudService)
    }

    val newtonRepository: NewtonRepository by lazy { NewtonRepository(RetrofitClient.newtonService) }
    val wealthRepository: WealthRepository by lazy { WealthRepository(RetrofitClient.wealthService) }
}
