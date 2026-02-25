package com.example.expomanager

import android.app.Application
import timber.log.Timber
import androidx.lifecycle.ViewModelProvider
import com.example.expomanager.model.database.Converters
import com.example.expomanager.model.database.ExpoManagerDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Clase ExpoManagerApplication que extiende Application
 * Configura y mantiene componentes globales para la aplicación (Firebase, Retrofit, ViewModel)
 */
class ExpoManagerApplication  : Application(){
    // Convertidor para transformar objetos (Event a EventEntity y viceversa)
    private val converters = Converters()
    // Autenticación de Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    // Retrofit para manejo de peticiones HTTP (API)
    lateinit var retrofit: Retrofit

    val eventViewModel: EventViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this).create(EventViewModel::class.java)
    }

    /**
     *  Método onCreate - inicializa Firebase, Timber y Retrofit
     *  Permite usar timber y que se muestren en los logs las actividades de oncreate, resume, destroy, ...
     */
    override fun onCreate() {
        super.onCreate()
        // Inicializa Firebase para la autenticación
        FirebaseApp.initializeApp(this)

        Timber.plant(Timber.DebugTree())
        // Configuración de la base de datos
        val db = ExpoManagerDatabase.getDatabase(this)
        val dao = db.eventDao()
        val predefinedEvents = sampleEvents.map { converters.toEntity(it) }
        // Cargar eventos predeterminados si la base de datos está vacía
        CoroutineScope(Dispatchers.IO).launch {
            val events = dao.getAllEventsList()
            if (events.isEmpty()) {
                dao.insertAll(predefinedEvents)
            }
        }
        // Configuración de Retrofit para API
        //Iniciamos retrofit
        retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        // Inicializar Firebase Auth para manejo de usuarios
        // Inicializar Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
        Timber.i("Firebase Auth inicializado correctamente.")
    }

    /**
     * Función que se encargar de obtener y crear una instancia de ApiService
     */
    fun getApiService(): ApiService {
        return retrofit.create(ApiService::class.java)
    }
    // Lista de eventos predeterminados para cargar al iniciar la aplicación
    //lista codificada como objeto acompañante ---> + accesible desde cualquier otra actividad
    companion object {
        val sampleEvents = mutableListOf<Event>(
            Event(
                    id = 1,
                    name = "Concierto de Rock",
                    descriptionShort = "Un increíble concierto con las mejores bandas de rock.",
                    description = "\nUn increíble concierto con las mejores bandas de rock.",
                    date = kotlin.collections.listOf(
                        "2025-05-15 20:00",
                        "2025-05-15 22:00",
                        "2025-05-18 19:30",
                        "2025-05-18 21:30"
                    ),
                    location = "\n\n\n\nMadison Square Garden, Nueva York",
                    imageUrl = com.example.expomanager.R.drawable.imagen_concierto_rock
            ),
            Event(
                id = 2,
                name = "Teatro de Broadway",
                descriptionShort = "Disfruta de una obra en el famoso teatro de Broadway.",
                description = "Esta primavera, vive la épica obra de Shakespeare como nunca antes.\n" +
                        "\n" +
                        "Denzel Washington, ganador de los premios Tony® y de la Academia®, interpreta a Otelo, el noble moro de Venecia y general guerrero al mando. Junto a él, Jake Gyllenhaal, nominado a los premios Tony® y de la Academia®, interpreta a Yago, el ambicioso teniente y hábil manipulador.\n" +
                        "\n" +
                        "Despreciado por su ascenso, la incansable búsqueda de venganza de Yago contra Otelo y su esposa, Desdémona (interpretada por Molly Osborne), los sumerge en una impactante red de engaños y traiciones.\n" +
                        "\n" +
                        "Dirigida por el ganador del premio Tony, Kenny Leon, esta producción de OTHELLO se presenta por un tiempo limitado de 15 semanas en Broadway únicamente hasta el 8 de junio.\n" +
                        "\n" +
                        "2 horas 35 minutos incluyendo un intermedio..",
                date = listOf("2025-08-01 11:00", "\n2025-08-01 13:00", "\n2025-08-04 19:30", "\n2025-08-04 23:15"),
                location = "\n\n\n\n243 West 47th Street, Nueva York, NY 10036, Estados Unidos",
                imageUrl = R.drawable.imagen_broadway
            ),
            Event(
                id = 3,
                name = "Conferencia de Tecnología",
                descriptionShort = "Asiste a la conferencia más grande sobre tecnología y desarrollo.",
                description = "\nAsiste a la conferencia más grande sobre tecnología y desarrollo.",
                date = listOf("2025-07-20 09:00", "2025-07-21 09:00", "2025-07-22 09:00"),
                location = "\n\n\n\nCentro de Convenciones, Madrid",
                imageUrl = R.drawable.imagen_ifema
            ),
            Event(
                id = 4,
                name = "Festival de Cine Independiente",
                descriptionShort = "Celebra el talento emergente en el cine de autor.",
                description = "\nCelebra el talento emergente en el cine de autor, con proyecciones exclusivas, charlas y más.",
                date = listOf("2025-08-10 18:00", "2025-08-11 18:00", "2025-08-12 18:00"),
                location = "\n\n\n\nCine Albéniz, C/ Alcazabilla, 4, Distrito Centro, 29015 Málaga",
                imageUrl = R.drawable.imagen_cine
            ),
            Event(
                id = 5,
                name = "Feria del Libro",
                descriptionShort = "Una semana dedicada a los libros, autores y lectores.",
                description = "\n2 semanas dedicadas a los libros, autores y lectores con presentaciones, firmas y talleres.",
                date = listOf("2025-05-30 10:00", "2025-06-15 10:00"),
                location = "\n\n\n\nParque del Retiro, Madrid",
                imageUrl = R.drawable.imagen_libros
            ),
            Event(
                id = 6,
                name = "Festival Magdalena en Vivo 2025",
                descriptionShort = "Festival musical con grandes artistas en Santander durante la Semana Grande.",
                description = "Sebastián Yatra, Manuel Carrasco, Edurne, Miguel Bosé, Nil Moliner y Marlon encabezan el cartel del Festival Magdalena en Vivo 2025.\n" +
                "\n" +
                "El evento celebra su quinta edición en la capital de Cantabria del 23 al 26 de julio de 2025, coincidiendo con las fiestas de la Semana Grande de Santander. Disfruta de un ambiente veraniego con las actuaciones de destacados artistas nacionales e internacionales.\n" +
                "\n" +
                "También se presentarán artistas como Maren, Mafalda Cardenal, Paula Koops, Lucía Gago y Javi Hierro DJ, entre otros.\n" +
                "\n" +
                "Un festival vibrante junto al mar que combina buena música, cultura y celebración en un entorno único.",
                date = listOf("2025-07-23 19:00", "2025-07-24 19:00", "2025-07-25 19:00", "2025-07-26 19:00"),
                location = "Campo de fútbol de La Magdalena, Santander, Cantabria, España",
                imageUrl = R.drawable.imagen_magdalena
                ),
            Event(
                id = 7,
                name = "Mad Cool Festival 2025",
                descriptionShort = "Festival internacional con artistas de renombre en Madrid.",
                description = "Mad Cool Festival celebra su octava edición del 10 al 12 de julio de 2025 en el espacio Iberdrola Music.\n" +
                        "\n" +
                        "El evento contará con actuaciones de Olivia Rodrigo, Kings of Leon, Noah Kahan, Gracie Abrams, Alanis Morissette, Weezer, Nine Inch Nails, Benson Boone, Justice, Iggy Pop, Thirty Seconds To Mars, Arde Bogotá, Residente, Glass Animals y St. Vincent.\n" +
                        "\n" +
                        "Este año se incorpora una jornada especial dedicada a la música electrónica: Brunch Electronik x Mad Cool, que tendrá lugar el domingo 13 de julio, con lo mejor del house, techno y beats modernos.\n" +
                        "\n" +
                        "Una experiencia musical imperdible para los amantes del rock, indie y electrónica en el corazón de Madrid.",
                date = listOf("2025-07-10 17:00", "2025-07-11 17:00", "2025-07-12 17:00", "2025-07-13 17:00"),
                location = "Iberdrola Music, Madrid, España",
                imageUrl = R.drawable.imagen_mad_cool_festival_2025
            )
        )
    }
    /**
     * Método para iniciar sesión con Firebase a partir de un correo y constraseña ambos
     * son strings
     * @return Inicio de sesión correcto o un error en otro caso
     */
    fun loginUser(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception?.localizedMessage ?: "Error desconocido")
                }
            }
    }
    /**
     * Método para verificar si el usuario está autenticado
     * @return TRUE si está autenticado, o FALSE en otro caso
     */
    fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

}