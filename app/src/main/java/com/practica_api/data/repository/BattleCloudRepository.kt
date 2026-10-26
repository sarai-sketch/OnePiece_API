package com.practica_api.data.repository

import com.practica_api.data.remote.client.RetrofitClient
import com.practica_api.data.remote.dto.SqliteCloudRequest
import com.practica_api.data.remote.service.SqliteCloudService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class BandaPirata(
    val id: String,
    val nombreBanda: String,
    val barcoPrincipal: String,
    val capitanNombre: String,
    val recompensaEstimada: Long,
    val territorioClave: String
)

data class ResultadoBatalla(
    val miBanda: BandaPirata,
    val rivalBanda: BandaPirata,
    val esVictoria: Boolean,
    val detalleCombate: String
)

class BattleCloudRepository(
    private val service: SqliteCloudService = RetrofitClient.sqliteCloudService
) {

    val bandasPredefinidas = listOf(
        BandaPirata("1", "Sombrero de Paja", "Thousand Sunny", "Monkey D. Luffy", 3000000000L, "Wano / Egghead"),
        BandaPirata("2", "Piratas de las Bestias", "Brontosaurus", "Kaido", 4611100000L, "Wano Country"),
        BandaPirata("3", "Piratas de Big Mom", "Queen Mama Chanter", "Charlotte Linlin", 4388000000L, "Whole Cake Island"),
        BandaPirata("4", "Piratas del Pelirrojo", "Red Force", "Shanks", 4048900000L, "Elbaf / Marineford"),
        BandaPirata("5", "Piratas de Barbanegra", "Saber of Xebec", "Marshall D. Teach", 3996000000L, "Hachinosu"),
        BandaPirata("6", "Piratas de Heart", "Polar Tang", "Trafalgar D. Water Law", 3000000000L, "Onigashima"),
        BandaPirata("7", "Piratas de Kid", "Victoria Punk", "Eustass Kid", 3000000000L, "Elbaf")
    )

    private suspend fun executeSql(sql: String): Result<List<Map<String, Any?>>> = withContext(Dispatchers.IO) {
        try {
            val response = service.executeQuery(
                RetrofitClient.SQLITE_CLOUD_API_KEY,
                SqliteCloudRequest(sql = sql, database = RetrofitClient.SQLITE_CLOUD_DB_NAME)
            )
            if (response.status == "success" || response.data != null) {
                Result.success(response.data.orEmpty())
            } else {
                Result.failure(Exception(response.error ?: "Error en la API de SQLite Cloud"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun simularBatallaNaval(miBanda: BandaPirata): Result<ResultadoBatalla> = withContext(Dispatchers.IO) {
        try {
            // Petición a la API de SQLite Cloud para consultar conectividad
            executeSql("SELECT 1;")

            val posiblesRivales = bandasPredefinidas.filter { it.nombreBanda != miBanda.nombreBanda }
            val rival = posiblesRivales.random()

            val suerteMarea = (85..125).random() / 100.0
            val poderMio = miBanda.recompensaEstimada * suerteMarea
            val poderRival = rival.recompensaEstimada.toDouble()

            val victoria = poderMio >= poderRival

            val detalle = if (victoria) {
                "🏆 ¡VICTORIA EN ALTA MAR!\n\n" +
                        "La tripulación '${miBanda.nombreBanda}' capitaneada por '${miBanda.capitanNombre}' a bordo del '${miBanda.barcoPrincipal}' ha derrotado en combate naval a la flota de '${rival.nombreBanda}' (${rival.capitanNombre}).\n\n" +
                        "¡Tu reputación ha aumentado en la Gran Línea!"
            } else {
                "💥 ¡DERROTA EN LA TORMENTA!\n\n" +
                        "La flota de '${rival.nombreBanda}' comandada por '${rival.capitanNombre}' sobrepasó la defensa del '${miBanda.barcoPrincipal}'.\n\n" +
                        "¡Reagrupa a tus piratas y prepara tu próximo abordaje!"
            }

            Result.success(ResultadoBatalla(miBanda, rival, victoria, detalle))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
