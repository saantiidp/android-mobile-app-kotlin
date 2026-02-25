package com.example.expomanager

/**
 * Modelo de datos de la información de ticket utilizado
 * para realizar las peticiones post y get con estos campos
 */
data class Ticket (
    // Nombre del evento al que pertenece el ticket
    val eventName: String,
    // Cantidad de tickets que se están comprando
    val ticketCount: Int,
    // El servidor genera este ID automáticamente
    val id: Int? = null // el id lo genera el server
)