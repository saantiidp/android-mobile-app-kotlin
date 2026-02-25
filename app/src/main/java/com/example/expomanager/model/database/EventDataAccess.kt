package com.example.expomanager.model.database

import androidx.lifecycle.LiveData

// Clase EventDataAccess que actúa como intermediario entre EventDao y las demás clases de la aplicación
class EventDataAccess(private val eventDao: EventDao) {
    // LiveData que expone todos los eventos en tiempo real
    val allEvents: LiveData<List<EventEntity>> = eventDao.getAllEvents()

    // Método para obtener todos los eventos como una lista (sin LiveData)
    // Esto es útil para operaciones que requieren una lista estática
    suspend fun getAllEventsList(): List<EventEntity> {
        return eventDao.getAllEventsList()
    }

    // Método para insertar todos los eventos en la base de datos
    // Esto es útil para restaurar eventos desde un respaldo o sincronizar con el servidor
    suspend fun insertAll(events: List<EventEntity>) {
        eventDao.insertAll(events)
    }
}
