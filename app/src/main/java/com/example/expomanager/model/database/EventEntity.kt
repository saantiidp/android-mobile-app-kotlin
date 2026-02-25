package com.example.expomanager.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

// Definición de la entidad EventEntity que representa un evento en la base de datos
@Entity(tableName = "events") // Anotación @Entity que indica que esta clase es una tabla en la base de datos
data class EventEntity(
    // Identificador único para cada evento (Primary Key)
    @PrimaryKey val id: Int, // Este ID es único para cada evento
    // Nombre del evento
    val name: String,
    // Descripción corta del evento (para vista previa)
    val descriptionShort: String,
    // Descripción completa del evento (detallada)
    val description: String,
    // Fecha del evento
    val date: String,  // Se separan con comas
    // Ubicación del evento
    val location: String,
    // URL de la imagen del evento
    val imageUrl: Int
)
