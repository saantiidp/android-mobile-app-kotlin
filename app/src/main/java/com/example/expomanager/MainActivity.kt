package com.example.expomanager
import  com.example.expomanager.databinding.ActivityMainBinding
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expomanager.model.database.Converters
import com.example.expomanager.model.database.CouponDataAccess
import com.example.expomanager.model.database.CouponEntity
import com.example.expomanager.model.database.EventDataAccess
import com.example.expomanager.model.database.EventEntity
import com.example.expomanager.model.database.ExpoManagerDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber


/**
 * Clase de datos Event que usamos para manejar la información de un evento
 * Usamos data para métodos toString, equals, hashCode..
 */
data class Event (
    val id: Int,
    val name: String,
    val descriptionShort: String,
    val description: String,
    val date: List<String>,
    val location: String,
    val imageUrl: Int
    )

/**
 * Clase MainActivity que extiende AppCompatActivity
 * Gestiona la pantalla principal de la aplicación donde se listan los eventos
 */
class MainActivity : AppCompatActivity() {
    // Configuración de DataBinding
    private lateinit var binding: ActivityMainBinding //Generado en el fichero de diseño con layout

    private lateinit var eventDataAccess: EventDataAccess
    private lateinit var couponDataAccess: CouponDataAccess
    // ViewModel para gestionar el estado y los eventos
    private val viewModel : MainViewModel by lazy {  // Instanciamos ViewModel con viewModelProvider, que gestiona el ciclo de vida y las vistas de la app
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    //Instanciamos el viewModel del evento
    // Obtenemos el EventViewModel de la aplicación de forma global definido
    private val eventViewModel: EventViewModel by lazy {
        (application as ExpoManagerApplication).eventViewModel
    }
    // Adaptador para el RecyclerView de eventos
    private lateinit var adapter: EventAdapter //Para recicler view

    //  Método onCreate - punto de entrada de la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Verificar si el usuario está autenticado
        if (!(application as ExpoManagerApplication).isUserLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        enableEdgeToEdge()
        //setContentView(R.layout.activity_main) //Sin databinding
        // Configuramos el DataBinding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        Timber.i("MainActivity cargada correctamente")
        //Timber.i("RESOURCE_TEST R.drawable.evento1 = + ${R.drawable.imagen_concierto_rock}");
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Inicializamos las instancias de acceso a los datos
        val database = ExpoManagerDatabase.getDatabase(this)
        eventDataAccess = EventDataAccess(database.eventDao())
        couponDataAccess = CouponDataAccess(database.couponDao())

        // Configuramos los botones para Backup y Restauración
        binding.buttonBackup?.setOnClickListener {
            backupEventsAndCouponsToFirebase()
        }

        binding.buttonRestore?.setOnClickListener {
            restoreEventsAndCouponsFromFirebase()
        }
        // Configura márgenes seguros para la pantalla
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Configurar RecyclerView con adaptador
        adapter = EventAdapter { selectedEvent ->
            eventViewModel.setEvent(selectedEvent)
            Timber.i("Evento seleccionado: ${selectedEvent.name}")
            val intent = Intent(this, EventInfoActivity::class.java)
            startActivity(intent)
        }
        binding.eventRecyclerView?.layoutManager = LinearLayoutManager(this)
        binding.eventRecyclerView?.adapter = adapter

        // Observar eventos en el ViewModel
        viewModel.events.observe(this) { events ->
            Timber.i("Eventos recibidos en MainActivity: ${events.size}")
            if (events.isNotEmpty()) {
                val converters = Converters()
                adapter.events = events.map { converters.toModel(it)}
                adapter.notifyDataSetChanged()
                Timber.i("Eventos recibidos en MainActivity: ${events.size}")
            } else {
                Toast.makeText(this, "No hay eventos disponibles", Toast.LENGTH_LONG).show()
            }
        }

        //Accedemos a los eventos definidos
        val events = ExpoManagerApplication.sampleEvents
        if (events.isNotEmpty()) {
            Toast.makeText(this, "Primer evento ahora: ${events[0].name}", Toast.LENGTH_LONG).show()
        }
        Timber.i("onCreate called")


    }


    //Métodos para manejar la vida de un ciclo de actividad
    override fun onStart() {
        super.onStart()
        Timber.i("onStart called")
    }
    override fun onResume() {
        super.onResume()
        Timber.i("onResume called")
    }
    override fun onPause() {
        super.onPause()
        Timber.i("onPause called")
    }
    override fun onStop() {
        super.onStop()
        Timber.i("onStop called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy called")
    }
    override fun onRestart() {
        super.onRestart()
        Timber.i("onRestart called")
    }
    /**
     * Función para realizar Backup de Eventos y Cupones en Firebase
     */
    private fun backupEventsAndCouponsToFirebase() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val events = eventDataAccess.getAllEventsList()
                val coupons = couponDataAccess.getAllCoupons()

                val reference = FirebaseDatabase.getInstance().getReference("backup")
                val backupData = mapOf(
                    "events" to events,
                    "coupons" to coupons
                )

                withContext(Dispatchers.Main) {
                    reference.setValue(backupData).addOnSuccessListener {
                        showToast("Backup completado en Firebase")
                        Timber.i("Backup completado en Firebase")
                    }.addOnFailureListener {
                        showToast("Error al hacer backup en Firebase")
                        Timber.e("Error al hacer backup en Firebase: ${it.message}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast("Error al hacer backup: ${e.message}")
                    Timber.e("Error al hacer backup: ${e.message}")
                }
            }
        }
    }

    /**
     * Función para Restaurar Eventos y Cupones desde Firebase
     */
    private fun restoreEventsAndCouponsFromFirebase() {
        val reference = FirebaseDatabase.getInstance().getReference("backup")
        reference.get().addOnSuccessListener { snapshot ->
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Restaurar Eventos
                    val eventsList = snapshot.child("events").children.mapNotNull {
                        val id = it.child("id").getValue(Int::class.java)
                        val name = it.child("name").getValue(String::class.java)
                        val descriptionShort = it.child("descriptionShort").getValue(String::class.java)
                        val description = it.child("description").getValue(String::class.java)
                        val dateRaw = it.child("date").getValue(String::class.java) ?: ""
                        val date = if (dateRaw.contains(",")) {
                            dateRaw.split(",").joinToString(", ")
                        } else {
                            dateRaw
                        }
                        val location = it.child("location").getValue(String::class.java)
                        val imageUrl = it.child("imageUrl").getValue(Int::class.java) ?: 0

                        if (id != null && name != null && location != null) {
                            EventEntity(
                                id = id,
                                name = name,
                                descriptionShort = descriptionShort ?: "",
                                description = description ?: "",
                                date = date,
                                location = location,
                                imageUrl = imageUrl
                            )
                        } else {
                            null
                        }
                    }

                    // Restaurar Cupones
                    val couponsList = snapshot.child("coupons").children.mapNotNull {
                        val id = it.child("id").getValue(Int::class.java) ?: 0
                        val eventId = it.child("eventId").getValue(Int::class.java) ?: 0
                        val eventName = it.child("eventName").getValue(String::class.java) ?: ""
                        val couponCode = it.child("couponCode").getValue(String::class.java) ?: ""
                        val timeStamp = it.child("timeStamp").getValue(Long::class.java) ?: System.currentTimeMillis()

                        if (eventId != 0 && eventName.isNotEmpty() && couponCode.isNotEmpty()) {
                            CouponEntity(
                                id = id,
                                eventId = eventId,
                                eventName = eventName,
                                couponCode = couponCode,
                                timeStamp = timeStamp
                            )
                        } else {
                            null
                        }
                    }

                    // Guardar eventos y cupones restaurados
                    eventDataAccess.insertAll(eventsList)
                    couponDataAccess.insertAllCoupons(couponsList)

                    withContext(Dispatchers.Main) {
                        showToast("Restauración completada correctamente")
                        Timber.i("Restauración completada con ${eventsList.size} eventos y ${couponsList.size} cupones")
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        showToast("Error al restaurar: ${e.message}")
                        Timber.e("Error al restaurar: ${e.message}")
                    }
                }
            }
        }.addOnFailureListener {
            showToast("Error al restaurar desde Firebase")
            Timber.e("Error al restaurar desde Firebase: ${it.message}")
        }
    }


    /**
     * Función para mostrar mensajes en pantalla
     */
    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Método que crea el menu con el icono de cierre de sesión
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_logout, menu)
        return true
    }

    /**
     * Método que gestiona la acción tras pulsar el eleemento definido
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logoutUser()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    /**
     * Método que realiza la salida de la sesión de usuario logueado, y lo
     * gestiona con firebase
     */
    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}