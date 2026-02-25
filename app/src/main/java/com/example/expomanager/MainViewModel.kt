package com.example.expomanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import timber.log.Timber
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.expomanager.model.database.Converters
import com.example.expomanager.model.database.EventDataAccess
import com.example.expomanager.model.database.EventEntity
import com.example.expomanager.model.database.ExpoManagerDatabase
import kotlinx.coroutines.launch

/**
 * Clase MainViewModel que extiende AndroidViewModel
 * Gestiona la lógica de los eventos y el estado de autenticación del usuario
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    // Acceso a la base de datos a través de EventDataAccess
    private val dataAccess: EventDataAccess
    // Conversor para transformar objetos de Event a EventEntity y viceversa
    private val converters = Converters()
    // LiveData que almacena la lista de eventos observables
    val events: LiveData<List<EventEntity>>
    // Estado del inicio de sesión (true si está autenticado, false si no)
    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> get() = _loginStatus
    // Referencia a la aplicación (ExpoManagerApplication)
    private val app = getApplication<ExpoManagerApplication>()
    // Inicialización del ViewModel
    init {
        // Obtenemos la base de datos y el acceso a eventos (EventDao)
        val dao = ExpoManagerDatabase.getDatabase(application).eventDao()
        dataAccess = EventDataAccess(dao)
        // Cargamos los eventos como LiveData desde la base de datos
        events = dataAccess.allEvents
    }

    // Método para agregar eventos si la lista está vacía
    fun addEventsIfEmpty(defaults: List<Event>) {
        viewModelScope.launch {
            if (events.value.isNullOrEmpty()) {
                dataAccess.insertAll(defaults.map { converters.toEntity(it) })
            }
        }
    }
    // Método para iniciar sesión en Firebase
    fun loginUser(email: String, password: String) {
        app.loginUser(email, password,
            onSuccess = {
                _loginStatus.postValue(true)
            },
            onFailure = { errorMessage ->
                _loginStatus.postValue(false)
                Timber.e("Login Error: $errorMessage")
            }
        )
    }

    // Verificar si el usuario está autenticado
    fun checkUserLoggedIn() {
        _loginStatus.postValue(app.isUserLoggedIn())
    }
}