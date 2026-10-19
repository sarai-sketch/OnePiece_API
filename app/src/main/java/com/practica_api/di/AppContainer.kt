package com.practica_api.di

import com.practica_api.data.remote.client.RetrofitClient
import com.practica_api.data.repository.NewtonRepository
import com.practica_api.data.repository.OnePieceRepository
import com.practica_api.data.repository.WealthRepository

/** Contenedor de dependencias manual: arma los repositorios con los servicios de RetrofitClient. */
object AppContainer {
    val onePieceRepository: OnePieceRepository by lazy { OnePieceRepository(RetrofitClient.onePieceService) }
    val newtonRepository: NewtonRepository by lazy { NewtonRepository(RetrofitClient.newtonService) }
    val wealthRepository: WealthRepository by lazy { WealthRepository(RetrofitClient.wealthService) }
}
