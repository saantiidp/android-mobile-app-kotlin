package com.example.expomanager.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
// Definición de la interfaz EventDao (Data Access Object)
// Esta interfaz define las operaciones de acceso a la base de datos para los eventos
@Dao
interface EventDao {
    // Método para obtener todos los eventos como LiveData (observables en tiempo real)
    @Query("SELECT * FROM events")
    fun getAllEvents(): LiveData<List<EventEntity>>

    // Método para obtener todos los eventos como una lista normal (sin LiveData)
    // Esto es útil si deseas manejar los eventos sin necesidad de observar cambios
    @Query("SELECT * FROM events")
    suspend fun getAllEventsList(): List<EventEntity>

    // Método para insertar una lista de eventos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<EventEntity>)

    // Método para insertar un solo evento
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: EventEntity)

    // Método para eliminar un evento
    @Delete
    suspend fun delete(event: EventEntity)

    //Método para acceder a un evento por su id
    @Query("SELECT * FROM events WHERE id = :eventId LIMIT 1")
    suspend fun getEventById(eventId: Int): EventEntity?
}
