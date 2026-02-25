package com.example.expomanager

import retrofit2.Response
import timber.log.Timber

/**
 * Clase que gestiona la lógica para hacer las peticiones de la api (post y get)
 */
class TicketRepository(private val api: ApiService) {

    suspend fun sendTicket(ticket: Ticket): Response<Ticket> {
        Timber.i("Enviando ticket: ${ticket.eventName}, cantidad: ${ticket.ticketCount}")
        return api.postTicket(ticket)
    }
    suspend fun getTicket(id: Int): Response<Ticket> {
        Timber.i("Obteniendo ticket con ID: $id")

        val response = api.getTicket(id)
        Timber.i("successful? ${response.isSuccessful}")
        if (response.isSuccessful) {
            val ticket = response.body()
            Timber.i("Ticket obtenido: $ticket")
        } else {
            Timber.e("Error en GET: ${response.code()}")
        }

        return response
    }
}