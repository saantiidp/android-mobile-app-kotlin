package com.example.expomanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

// Clase EventViewModel que extiende ViewModel
// Gestiona la lógica de los eventos y mantiene el estado del evento seleccionado
class EventViewModel: ViewModel() {

    //Variables para mostrar los datos del evento al pulsar su button
    val isDescrpVisible = MutableLiveData(false) // Controla la visibilidad de la descripción
    val isLoctVisible = MutableLiveData(false) // Controla la visibilidad de la ubicación
    val isDateVisible = MutableLiveData(false) // Controla la visibilidad de la fecha


    //Usamos la variable privada para evitar que se accede a los datos por otras clases y lo cambien
    //Livedata con el evento que se ha seleccionaado
    private val _selectedEvent = MutableLiveData<Event>()
    // LiveData pública para observar el evento seleccionado (solo lectura)
    val selectedEvent: LiveData<Event>
        get() = _selectedEvent

    // Inicialización del ViewModel
    init {
        Timber.i("Evento en INIT seleccionado: ${_selectedEvent.value?.name}")
    }
    // Método para establecer el evento seleccionado
    fun setEvent (evento: Event) {
        _selectedEvent.value= evento
        Timber.i("Evento seleccionado en ViewModel AQUII: ${evento.name} y el valor de _selectedEvent es ${_selectedEvent.value!!.name} y el de selectedEvent ${selectedEvent.value?.name}")
    }

    // Método para mostrar solo la descripción del evento
    fun showDescription() {
        isDescrpVisible.value = true
        isLoctVisible.value = false
        isDateVisible.value = false
    }
    // Método para mostrar solo la ubicación del evento
    fun showLocation() {
        isDescrpVisible.value = false
        isLoctVisible.value = true
        isDateVisible.value = false
    }
    // Método para mostrar solo la fecha del evento
    fun showDate() {
        isDescrpVisible.value = false
        isLoctVisible.value = false
        isDateVisible.value = true
    }

}