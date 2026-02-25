package com.example.expomanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

// Clase LoginActivity que extiende AppCompatActivity
// Gestiona el proceso de inicio de sesión del usuario
class LoginActivity : AppCompatActivity() {
    // Inicialización del ViewModel (MainViewModel) utilizando el patrón viewModels
    private val mainViewModel: MainViewModel by viewModels()
    // Método onCreate - punto de entrada de la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Referencias a los campos de correo y contraseña
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        // Observamos el estado de inicio de sesión (loginStatus) desde el ViewModel
        mainViewModel.loginStatus.observe(this, Observer { isLoggedIn ->
            if (isLoggedIn) {
                // Si el inicio de sesión es exitoso, navegamos a TitleActivity
                startActivity(Intent(this, TitleActivity::class.java))
                finish()// Cerramos la actividad de inicio de sesión para no volver atrás
            } else {
                // Si el inicio de sesión falla, mostramos un mensaje de error
                Toast.makeText(this, "Error de inicio de sesión", Toast.LENGTH_SHORT).show()
            }
        })
        // Configuración del botón de inicio de sesión
        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString().trim() // Obtenemos el correo ingresado
            val password = editTextPassword.text.toString().trim() // Obtenemos la contraseña


            if (email.isNotEmpty() && password.isNotEmpty()) {
                mainViewModel.loginUser(email, password)// Iniciamos sesión a través del ViewModel
            } else {
                // Mostramos un mensaje si los campos están vacíos
                Toast.makeText(this, "Por favor, ingrese su correo y contraseña", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
