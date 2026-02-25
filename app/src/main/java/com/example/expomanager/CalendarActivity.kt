package com.example.expomanager

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.expomanager.databinding.ActivityCalendarBinding
import timber.log.Timber

// Definición de la clase CalendarActivity, que gestiona el calendario de eventos
class CalendarActivity : AppCompatActivity() {

    // Instanciamos el ViewModel usando viewModels() (propio de Jetpack)
    private val calendarViewModel: CalendarViewModel by viewModels()

    // Método onCreate - punto de entrada de la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configuración de DataBinding para enlazar la interfaz XML (activity_calendar.xml)
        val binding: ActivityCalendarBinding = DataBindingUtil.setContentView(this, R.layout.activity_calendar)
        // Vinculamos el ViewModel al diseño para que pueda ser usado directamente en XML
        binding.calendarViewModel = calendarViewModel
        binding.lifecycleOwner = this

        //Obtenemos las fechas tras el envío por eventActivity con intent
        val eventDate = intent.getStringExtra("EVENT_DATE")
        eventDate?.let { calendarViewModel.setEventDate(it) }// Asignamos la fecha recibida al ViewModel

        //Manejamos el viewmodel, para que actualice la fecha indicada por el usuario
        val calendarView = binding.calendarView
        calendarView.setOnDateChangeListener {_, year, month, day ->
            // Llamamos al método del ViewModel para actualizar la fecha seleccionada
            calendarViewModel.onDateSelected(year, month, day) //Método de viewModel del calendario
        }
        // Botón para regresar a la vista anterior
        binding.buttonBackEvents.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
        // Registramos en el log que la actividad ha sido creada
        Timber.i("OnCreate called")
    }

    //Métodos para manejar la vida de un ciclo de actividad
    override fun onStart() {
        super.onStart()
        Timber.i("onStart called")
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume called")
        //código que actualiza la interfaz
        //binding.invalidateAll()
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
