package com.practica_api

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.practica_api.core.cleaned
import com.practica_api.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener { onLogin() }
        binding.btnRegister.setOnClickListener { onRegister() }
    }

    private fun onLogin() {
        val email = binding.emailInput.text?.toString().cleaned()
        val password = binding.passwordInput.text?.toString().cleaned()

        if (!validate(email, password)) return

        setLoading(true)
        auth.signInWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(this) { task ->
                setLoading(false)
                if (task.isSuccessful) {
                    Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                    openMain()
                } else {
                    showError(task.exception?.localizedMessage ?: "Error al iniciar sesión")
                }
            }
    }

    private fun onRegister() {
        val email = binding.emailInput.text?.toString().cleaned()
        val password = binding.passwordInput.text?.toString().cleaned()

        if (!validate(email, password)) return

        setLoading(true)
        auth.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(this) { task ->
                setLoading(false)
                if (task.isSuccessful) {
                    Toast.makeText(this, getString(R.string.register_success), Toast.LENGTH_SHORT).show()
                    openMain()
                } else {
                    showError(task.exception?.localizedMessage ?: "Error al crear cuenta pirata")
                }
            }
    }

    private fun validate(email: String?, password: String?): Boolean {
        binding.emailLayout.error = if (email.isNullOrEmpty()) getString(R.string.error_required) else null
        binding.passwordLayout.error = if (password.isNullOrEmpty()) getString(R.string.error_required) else null
        return !email.isNullOrEmpty() && !password.isNullOrEmpty()
    }

    private fun setLoading(loading: Boolean) {
        binding.loginProgress.isVisible = loading
        binding.btnLogin.isEnabled = !loading
        binding.btnRegister.isEnabled = !loading
        binding.errorText.isVisible = false
    }

    private fun showError(message: String) {
        binding.errorText.text = message
        binding.errorText.isVisible = true
    }

    private fun openMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
