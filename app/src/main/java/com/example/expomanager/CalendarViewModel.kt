package com.example.expomanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// Definición de la clase CalendarViewModel que extiende ViewModel
class CalendarViewModel: ViewModel() {

    // Variable privada para almacenar la fecha seleccionada (MutableLiveData)
    private val _selectedDate = MutableLiveData<String>()
    // Variable privada para almacenar la fecha del evento (MutableLiveData)
    private val _eventDate = MutableLiveData<String>()


    // Variables públicas que exponen las fechas como LiveData
    val selectedDate: LiveData<String>
        get() = _selectedDate // Fecha seleccionada por el usuario

    val eventDate: LiveData<String>
        get() = _eventDate // Fecha del evento

    //Método que actualiza la fecha al seleccionarlo el usuario
    fun onDateSelected(year: Int, month: Int, day: Int) {
        // Formato de la fecha: Día/Mes/Año
        _selectedDate.value = "$day/${month + 1}/$year"
    }


    // Método que asigna la fecha del evento (recibida desde otra actividad)
    fun setEventDate(date: String){
        _eventDate.value = date
    }
}