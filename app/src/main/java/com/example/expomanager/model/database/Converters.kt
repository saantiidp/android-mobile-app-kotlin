package com.example.expomanager.model.database

import com.example.expomanager.Event


// Clase Converters que proporciona métodos para convertir entre objetos Event y EventEntity
class Converters {
    // Método para convertir un objeto Event a EventEntity
    fun toEntity(event: Event): EventEntity = EventEntity(
        id = event.id,                           // Identificador único del evento
        name = event.name,                       // Nombre del evento
        descriptionShort = event.descriptionShort, // Descripción corta del evento
        description = event.description,         // Descripción completa del evento
        date = event.date.joinToString(","),     // Fecha del evento como una cadena separada por comas
        location = event.location,               // Ubicación del evento
        imageUrl = event.imageUrl                // URL de la imagen del evento
    )
    /**
     * toEntity:
     * - Convierte un objeto Event (modelo) en un EventEntity (entidad de base de datos).
     * - Esto es necesario porque EventEntity se almacena directamente en la base de datos,
     *   mientras que Event se utiliza en la interfaz y lógica de la aplicación.
     * - La fecha se convierte en una cadena separada por comas para que se almacene como texto.
     */
    // Método para convertir un objeto EventEntity a Event
    fun toModel(entity: EventEntity): Event = Event(
        id = entity.id,                         // Identificador único del evento
        name = entity.name,                     // Nombre del evento
        descriptionShort = entity.descriptionShort, // Descripción corta del evento
        description = entity.description,       // Descripción completa del evento
        date = entity.date.split(","),          // Fecha convertida de cadena a lista de fechas
        location = entity.location,             // Ubicación del evento
        imageUrl = entity.imageUrl              // URL de la imagen del evento
    )
/**
 * toModel:
 * - Convierte un objeto EventEntity (entidad de base de datos) en un Event (modelo).
 * - Esto es necesario porque Event se utiliza en la interfaz y lógica de la aplicación,
 *   mientras que EventEntity se almacena directamente en la base de datos.
 * - L- La fecha se convierte de texto (separado por comas) a una lista de fechas.
 *      */
}

