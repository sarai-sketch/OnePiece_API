package com.practica_api

import android.content.Intent
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.practica_api.databinding.ActivitySplashBinding
import com.practica_api.di.AppContainer
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

/**
 * Pantalla de bienvenida: presenta la app con una animación y, mientras tanto,
 * precarga las listas de One Piece para que la pantalla principal abra al instante.
 */
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // Debe ir antes de super.onCreate: toma el relevo de la splash del sistema (Android 12+ y anteriores).
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animateIn()

        lifecycleScope.launch {
            val preload = launch { preloadData() }

            // Mensajes de carga mientras se trabaja en segundo plano (mínimo ~2 s de bienvenida).
            resources.getStringArray(R.array.splash_messages).forEach { message ->
                binding.statusText.text = message
                delay(MESSAGE_DELAY_MS)
            }

            // Si la red es lenta no bloqueamos: cada pantalla reintenta sola.
            withTimeoutOrNull(MAX_WAIT_MS) { preload.join() }
            openMain()
        }
    }

    private suspend fun preloadData() = coroutineScope {
        val repository = AppContainer.onePieceRepository
        launch { repository.getCharacters() }
        launch { repository.getFruits() }
        launch { repository.getSagas() }
    }

    private fun animateIn() = with(binding) {
        logo.scaleX = 0.5f
        logo.scaleY = 0.5f
        logo.alpha = 0f
        logo.animate().alpha(1f).scaleX(1f).scaleY(1f)
            .setDuration(800).setInterpolator(OvershootInterpolator()).start()

        listOf(titleText, taglineText).forEachIndexed { index, view ->
            view.alpha = 0f
            view.translationY = 40f
            view.animate().alpha(1f).translationY(0f)
                .setStartDelay(400L + index * 200L).setDuration(600).start()
        }
    }

    private fun openMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private companion object {
        const val MESSAGE_DELAY_MS = 750L
        const val MAX_WAIT_MS = 6_000L
    }
}
