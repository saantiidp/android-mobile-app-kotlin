package com.example.expomanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.expomanager.databinding.ActivityBuyTicketBinding

// Definición de la clase BuyTicketActivity que extiende AppCompatActivity
class BuyTicketActivity: AppCompatActivity() {
    // Variable de DataBinding para enlazar la interfaz de usuario (XML) con esta actividad
    lateinit var binding: ActivityBuyTicketBinding
    // Método onCreate - punto de entrada de la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configuramos la interfaz de usuario usando DataBinding
        // Esto permite acceder a los elementos de la interfaz
        binding = DataBindingUtil.setContentView(this, R.layout.activity_buy_ticket)

        // Verificamos si la actividad se está creando por primera vez
        if (savedInstanceState == null) {

            // Creamos una instancia del fragmento BuyTicketFragment
            val fragment = BuyTicketFragment().apply {
                // Usamos un Bundle para pasar datos al fragmento
                arguments = Bundle().apply {
                    // Pasamos el ID del evento desde el Intent (actividad que lanza esta)
                    putInt("eventId", intent.getIntExtra("eventId", -1))
                    // Pasamos el nombre del evento desde el Intent
                    putString("eventName", intent.getStringExtra("eventName"))
                }
            }
            // Reemplazamos el contenedor del fragmento (buyTicketFragment) por este fragmento creado
            supportFragmentManager.beginTransaction()
                .replace(R.id.buyTicketFragment, fragment)// ID del contenedor donde se insertará el fragmento
                .commit() // Aplicamos la transacción
        }
    }
}