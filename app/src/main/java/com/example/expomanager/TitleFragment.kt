package com.example.expomanager

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.expomanager.databinding.FragmentTitleBinding

// Definición de la clase TitleFragment que extiende Fragment
class TitleFragment: Fragment() {

    // Variable de DataBinding para enlazar la interfaz de usuario (XML) con este fragmento
    private lateinit var binding: FragmentTitleBinding

    // Método que crea y retorna la vista del fragmento
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inicialización de DataBinding, vinculando el XML con este fragmento
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_title, // Diseño asociado (fragment_title.xml)
            container,
            false
        )

        // Configuración del botón (titleSubtitleApp) para navegar a MainActivity
        binding.titleSubtitleApp.setOnClickListener {
            // Iniciar la actividad MainActivity al hacer clic
            startActivity(Intent(activity, MainActivity::class.java))
        }
        // Retornar la vista configurada
        return binding.root
    }
}
