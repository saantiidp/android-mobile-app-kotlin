package com.example.expomanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import timber.log.Timber

// Clase TicketViewModel que extiende ViewModel
// Esta clase gestiona la lógica de negocio para la compra y recuperación de tickets
class TicketViewModel(private val repository: TicketRepository) : ViewModel() {
    // MutableLiveData para el ticket, expuesto como LiveData
    private val _ticket = MutableLiveData<Ticket?>() // Ticket enviado u obtenido
    val ticket: MutableLiveData<Ticket?> = _ticket // Público para ser observado en la interfaz

    // MutableLiveData para errores, expuesto como LiveData
    private val _error = MutableLiveData<String>() // Mensajes de error
    val error: LiveData<String> = _error // Público para ser observado en la interfaz

    // Método para enviar un ticket al servidor (compra de tickets)
    fun sendTicket(eventName: String, ticketCount: Int) {
        viewModelScope.launch {
            try {
                // Crear un objeto Ticket con la información proporcionada
                val ticketToSend = Ticket(eventName = eventName, ticketCount = ticketCount, id = 1)

                // Enviar el ticket usando el repositorio (API)
                val posted = repository.sendTicket(ticketToSend)
                // Verificar si la respuesta es exitosa
                if (posted.isSuccessful) {
                    _ticket.postValue(posted.body())  // Publicar el ticket recibido
                    Timber.i("Ticket enviado correctamente")
                } else {
                    // En caso de error, publicamos el mensaje de error
                    _error.postValue("Error en POST: ${posted.code()}")
                    Timber.w("POST fallido con código: ${posted.code()}")
                }
            } catch (e: Exception) {
                // Capturar excepciones y registrar en el log
                Timber.e(e, "Error en sendTicket")
                _error.postValue("Error: ${e.message}")
            }
        }
    }
    // Método para obtener un ticket del servidor (consulta de tickets)
    fun fetchTicket(id: Int) {
        viewModelScope.launch {
            try {
                // Realizamos la solicitud GET al servidor usando el repositorio
                val fetched = repository.getTicket(1)
                // Verificar si la respuesta es exitos
                if (fetched.isSuccessful) {
                    val ticket = fetched.body()  // Obtenemos el ticket del cuerpo de la respuesta
                    if (ticket != null) {
                        _ticket.postValue(ticket) // Publicamos el ticket obtenido
                        Timber.i("Ticket obtenido: $ticket")
                    } else {
                        // Manejo de caso donde el cuerpo es nulo
                        _error.postValue("Ticket no encontrado")
                        Timber.w("El cuerpo de la respuesta es null")
                    }
                } else {
                    // Publicar el error si la respuesta no es exitosa
                    _error.postValue("Error en GET: ${fetched.code()}")
                    Timber.w("GET fallido con código: ${fetched.code()}")
                }
            } catch (e: Exception) {
                // Capturar excepciones y registrar en el log
                Timber.e(e, "Error en fetchTicket")
                _error.postValue("Excepción en GET: ${e.message}")
            }
        }
    }

}