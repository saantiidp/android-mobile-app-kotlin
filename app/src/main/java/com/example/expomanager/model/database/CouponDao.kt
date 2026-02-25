package com.example.expomanager.model.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// Definición de la interfaz CouponDao (Data Access Object)
// Esta interfaz define las operaciones de acceso a la base de datos para los cupones
@Dao
interface CouponDao {
    // Método para insertar un solo cupón en la base de datos
    // Si el cupón ya existe (mismo ID), se reemplaza por el nuevo (OnConflictStrategy.REPLACE)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoupon(coupon: CouponEntity)

    // Método para insertar una lista de cupones en la base de datos
    // Utiliza la misma estrategia de reemplazo para evitar conflictos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCoupons(coupons: List<CouponEntity>)

    // Método para obtener todos los cupones almacenados en la base de datos
    // Es suspendido (coroutine) porque la consulta se ejecuta en segundo plano (hilo IO)
    @Query("SELECT * FROM coupons")
    suspend fun getAllCoupons(): List<CouponEntity>

    // Método para obtener cupones específicos relacionados a un evento (eventId)
    // Retorna un LiveData, lo que permite observar los cupones en tiempo real
    @Query("SELECT * FROM coupons WHERE eventId = :eventId")
    fun getCouponsForEvents(eventId: Int): LiveData<List<CouponEntity>>
}
