package com.example.expomanager

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.expomanager.databinding.ActivityEventInfoBinding
import timber.log.Timber

// Clase EventInfoActivity que extiende AppCompatActivity
// Gestiona la vista de información detallada de un evento
class EventInfoActivity : AppCompatActivity() {
    // Variable de DataBinding para enlazar la interfaz de usuario (XML) con esta actividad
    private lateinit var binding: ActivityEventInfoBinding

    //Usamos eventViewModel como actividad global, con una única instancia en la app
    private val eventViewModel: EventViewModel by lazy {
        (application as ExpoManagerApplication).eventViewModel
    }
    // Método onCreate - punto de entrada de la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_info)

        // Configura márgenes seguros para la pantalla
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicialización de DataBinding para enlazar el XML directamente
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_info)
        binding.lifecycleOwner = this
        binding.eventViewModel = eventViewModel

        // Configurar el Observer para observar los cambios en el evento seleccionado
        eventViewModel.selectedEvent.observe(this) { event ->
            event?.let {
                Timber.i("Evento mostrado ${it.name}")
                // Actualizar los elementos de la interfaz con la información del evento
                binding.eventName.text = it.name
                binding.eventDescription.text = it.description
                binding.eventDate.text = it.date.joinToString("\n")
                binding.eventLocation.text = it.location
                binding.eventImage.setImageResource(it.imageUrl)
            }
        }
        // Configuración de botones para mostrar información específica del evento
        binding.buttonIconDescription.setOnClickListener{
            eventViewModel.showDescription()// Muestra la descripción completa del evento
        }

        binding.buttonIconLocation.setOnClickListener{
            eventViewModel.showLocation() // Muestra la ubicación del evento
        }
        // Botón para ver la fecha del evento en el calendario
        binding.buttonCalendar.setOnClickListener{
            eventViewModel.showDate()
            val event = eventViewModel.selectedEvent.value
            val intent = Intent(this, CalendarActivity::class.java).apply {
                putExtra("EVENT_DATE", event?.date?.joinToString("\n"))
            }
            startActivity(intent)
        }
        // Botón para comprar entradas del evento
        binding.buttonBuyTickets?.setOnClickListener{
            val event = eventViewModel.selectedEvent.value
            val intent = Intent(this, BuyTicketActivity::class.java).apply {
                putExtra("eventId", event?.id)
                putExtra("eventName", event?.name)
            }
            startActivity(intent)
        }

        // Botón para regresar a la lista de eventos
        binding.buttonBackEvents.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        Timber.i("onCreate called")
    }

    //Métodos para manejar la vida de un ciclo de actividad
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
