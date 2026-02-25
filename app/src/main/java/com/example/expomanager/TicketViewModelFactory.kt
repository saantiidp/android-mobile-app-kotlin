package com.example.expomanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// Clase TicketViewModelFactory que implementa ViewModelProvider.Factory
// Esta clase es responsable de crear instancias de TicketViewModel
class TicketViewModelFactory(private val repository: TicketRepository) : ViewModelProvider.Factory {
    // Método create que se llama para crear un nuevo ViewModel
    override fun <T : ViewModel> create(modelClass: Class<T>): T {  // Aquí usamos <T : ViewModel>
        // Verificamos si el modelo solicitado es TicketViewModel
        if (modelClass.isAssignableFrom(TicketViewModel::class.java)) {
            // Si es TicketViewModel, retornamos una nueva instancia del ViewModel
            return TicketViewModel(repository) as T
        }
        // Si el modelo solicitado no es TicketViewModel, lanzamos una excepción
        throw IllegalArgumentException("Error, desconocido ViewModel class")
    }
}
