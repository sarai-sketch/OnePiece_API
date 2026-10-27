package com.practica_api.di

import android.content.Context
import com.practica_api.data.remote.client.RetrofitClient
import com.practica_api.data.repository.BattleCloudRepository
import com.practica_api.data.repository.FavoritosRepository
import com.practica_api.data.repository.NewtonRepository
import com.practica_api.data.repository.OnePieceRepository
import com.practica_api.data.repository.WealthRepository

/** Contenedor de dependencias manual: arma los repositorios exclusivamente con RetrofitClient. */
object AppContainer {

    fun init(context: Context) {
        // Inicializador de contenedor si se requiere
    }

    val onePieceRepository: OnePieceRepository by lazy {
        OnePieceRepository(RetrofitClient.onePieceService)
    }

    val favoritosRepository: FavoritosRepository by lazy {
        FavoritosRepository(RetrofitClient.edgeFunctionService)
    }

    fun getFavoritosRepository(context: Context? = null): FavoritosRepository = favoritosRepository

    val battleCloudRepository: BattleCloudRepository by lazy {
        BattleCloudRepository(RetrofitClient.sqliteCloudService)
    }

    val newtonRepository: NewtonRepository by lazy { NewtonRepository(RetrofitClient.newtonService) }
    val wealthRepository: WealthRepository by lazy { WealthRepository(RetrofitClient.wealthService) }
}
