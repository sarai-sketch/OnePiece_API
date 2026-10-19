package com.practica_api.core

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.gson.JsonParseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

/** Ejecuta una llamada de red y devuelve Result sin lanzar excepciones (salvo cancelación). */
suspend fun <T> safeApiCall(block: suspend () -> T): Result<T> =
    try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

/** Traduce excepciones técnicas a mensajes entendibles para el usuario. */
fun Throwable.toUserMessage(): String = when (this) {
    is UnknownHostException, is ConnectException -> "Sin conexión a internet."
    is SocketTimeoutException -> "El servidor tardó demasiado en responder."
    is HttpException -> "Error del servidor (código ${code()})."
    is IOException -> "Problema de red. Intenta de nuevo."
    is JsonParseException -> "La respuesta del servidor no tiene el formato esperado."
    else -> "Ocurrió un error inesperado."
}

/** Recolecta un Flow respetando el ciclo de vida de la vista del Fragment. */
fun <T> Fragment.collectFlow(flow: Flow<T>, block: (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect { block(it) }
        }
    }
}

/** Devuelve null si el texto es nulo, vacío o un guion (la API usa "-" para "sin dato"). */
fun String?.cleaned(): String? = this?.trim()?.takeIf { it.isNotEmpty() && it != "-" }
