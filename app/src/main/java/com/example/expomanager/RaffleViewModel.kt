package com.example.expomanager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.expomanager.model.database.CouponDataAccess
import com.example.expomanager.model.database.CouponEntity
import com.example.expomanager.model.database.ExpoManagerDatabase
import kotlinx.coroutines.launch
import kotlin.random.Random

// Clase RaffleViewModel que extiende AndroidViewModel para gestionar la lógica de la rifa
class RaffleViewModel(application: Application) : AndroidViewModel(application){
    // Clase RaffleViewModel que extiende AndroidViewModel para gestionar la lógica de la rifa
    private var liveData = MutableLiveData<Int>()
    // Acceso a la base de datos (ExpoManagerDatabase) para gestionar cupones
    private val dataBase = ExpoManagerDatabase.getDatabase(application) // Base de datos local
    private val couponDBaccess = CouponDataAccess(dataBase.couponDao())// DAO de cupones

    // LiveData que expone el número generado como un observable
    var number: LiveData<Int> = MutableLiveData<Int>()
        get() = liveData // Retorna el número generado
    // Método para generar un número aleatorio entre 0 y 99
    fun generateNumber() {
        liveData.value = Random.nextInt(100)
    }

    val isRaffleWinner: LiveData<Boolean> = number.switchMap(::transform)


    // Método para transformar el número en un booleano (si es ganador o no)
    private fun transform(num: Int): MutableLiveData<Boolean> {
        return if(num % 2 == 0) // Si el número es par, es ganador
            MutableLiveData<Boolean>().apply { value = true }
        else
            MutableLiveData<Boolean>().apply { value = false }
    }
    // Método para generar un código aleatorio para el cupón
    fun generateRandomString(length: Int): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789" // Conjunto de caracteres permitidos
        return (1..length) // Genera un string de la longitud especificada
            .map { charset.random() }  // Elige caracteres al azar
            .joinToString("") // Une los caracteres en un solo string
    }

    //Función para que guarde el cupón en la base de datos
    fun saveCouponDBwinner(eventId: Int, eventName: String, code: String) {
        // Creamos el objeto cupón
        val cupon = CouponEntity(
            eventId = eventId,
            eventName = eventName,
            couponCode = code)
        // Guardamos el cupón en la base de datos usando una coroutine
        viewModelScope.launch { couponDBaccess.insertCoupon(cupon) }
    }
}
