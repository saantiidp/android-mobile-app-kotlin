package com.example.expomanager.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

// Definición de la entidad CouponEntity que representa un cupón en la base de datos
@Entity(tableName = "coupons")  // Anotación @Entity que indica que esta clase es una tabla en la base de dato
data class CouponEntity (
    // Identificador único para cada cupón (Primary Key)
    @PrimaryKey(autoGenerate = true) val id: Int = 0, //Se genera un id aleatorio para el cupón
    // ID del evento al que está asociado el cupón
    val eventId: Int,// Esto permite relacionar el cupón con un evento específico
    // Nombre del evento asociado al cupón
    val eventName: String,// Permite mostrar a qué evento pertenece el cupón
    // Código del cupón (el código que se utiliza para canjear)
    val couponCode: String,// Identificador único del cupón
    // Marca de tiempo que indica cuándo se creó el cupón
    val timeStamp: Long = System.currentTimeMillis()// Se inicializa con el tiempo actual
)
