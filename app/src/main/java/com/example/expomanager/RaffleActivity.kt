package com.example.expomanager

import android.os.Bundle
import android.view.View.INVISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.expomanager.databinding.ActivityRaffleBinding

// Clase RaffleActivity que extiende AppCompatActivity
// Gestiona la actividad principal de la rifa, que contiene los fragmentos de la rifa
class RaffleActivity: AppCompatActivity() {
    // Variable de DataBinding para enlazar la interfaz de usuario (XML) con esta actividad
    lateinit var binding: ActivityRaffleBinding

    // Inicialización del ViewModel (RaffleViewModel) utilizando el patrón laz
    private val viewModel: RaffleViewModel by lazy {
        ViewModelProvider(this).get(RaffleViewModel::class.java)
    }

    // Método onCreate - punto de entrada de la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_raffle)
        // Configuración del botón para participar en la rifa
        //Gestión de los fragmentos con navigation
        binding.buttonParticipateRaffle.setOnClickListener{
            binding.buttonParticipateRaffle.visibility = INVISIBLE // Oculta el botón después de hacer clic
            viewModel.generateNumber()// Genera un número aleatorio para la rifa
        }
        // Configuración del botón para verificar el número generado
        binding.buttonCheckNumber.setOnClickListener {
            // Verificamos el fragmento actual en el NavHost
            val currentFragment = findNavController(R.id.raffle_nav_host)
            // Navegamos al fragmento de resultados si estamos en el fragmento principal
            val destinationFragment = currentFragment.currentDestination?.id

            if (destinationFragment == R.id.raffleMainFragment) {
                currentFragment.navigate(R.id.action_raffleMainFragment_to_raffleSecondFragment)
            }
            // Ocultamos el botón después de hacer clic
            binding.buttonCheckNumber.visibility = INVISIBLE
        }
        // Configuración del botón para regresar a la lista de evento
        binding.buttonBackEvents.setOnClickListener{
            finish() // Finaliza la actividad y regresa a la anterior
        }
    }
}