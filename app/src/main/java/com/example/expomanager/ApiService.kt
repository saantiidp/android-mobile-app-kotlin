package com.example.expomanager

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Interfaz de retrofit, con los métodos definidos para
 * realizar las peticiones post y get
 */
interface ApiService {
    // Método POST para enviar un ticket al servidor
    @POST("comments")
    suspend fun postTicket(@Body ticket: Ticket): Response<Ticket>
    /**
     * postTicket:
     * - Envia un ticket al servidor utilizando una solicitud HTTP POST.
     * - @Body: El cuerpo de la solicitud contiene el objeto Ticket.
     * - @return Response<Ticket>: La respuesta del servidor, que contiene el ticket enviado.
     */

    // Método GET para obtener un ticket del servidor por su ID
    @GET("comments/{id}")
    suspend fun getTicket(@Path("id") id: Int): Response<Ticket>
    /**
     * getTicket:
     * - Obtiene un ticket del servidor por su ID utilizando una solicitud HTTP GET.
     * - @Path: El ID del ticket se pasa en la URL como un parámetro.
     * - @return Response<Ticket>: La respuesta del servidor, que contiene el ticket obtenido.
     */
}