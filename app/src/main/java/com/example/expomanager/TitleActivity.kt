package com.example.expomanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.expomanager.databinding.ActivityTitleBinding
import timber.log.Timber

// Definición de la clase TitleActivity que extiende AppCompatActivity
class TitleActivity : AppCompatActivity() {

    // Variable de DataBinding para enlazar la interfaz de usuario (XML) con esta actividad
    private lateinit var binding: ActivityTitleBinding

    // Método onCreate - punto de entrada de la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configuración de DataBinding para enlazar la interfaz XML (activity_title.xml)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_title)



    }

    // Métodos del ciclo de vida de la actividad
    override fun onStart() {
        super.onStart()
        Timber.i("onStart called")
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume called")
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause called")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy called")
    }

    override fun onRestart() {
        super.onRestart()
        Timber.i("onRestart called")
    }
}
