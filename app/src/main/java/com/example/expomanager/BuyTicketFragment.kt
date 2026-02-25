package com.example.expomanager

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.expomanager.databinding.FragmentBuyTicketBinding
import com.example.expomanager.model.database.EventDao
import com.example.expomanager.model.database.EventEntity
import com.example.expomanager.model.database.ExpoManagerDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

// Clase BuyTicketFragment que extiende Fragment para gestionar la compra de entradas
class BuyTicketFragment : Fragment() {
    // Variables globales
    private var eventName: String? = null // Nombre del evento a comprar
    private var eventId: Int = -1 // ID del evento a comprar
    private lateinit var binding: FragmentBuyTicketBinding // DataBinding para la vista
    private lateinit var eventDao: EventDao // DAO para acceder a los eventos en la base de datos
    private var eventRaffle: EventEntity? = null // Evento actual que se está comprando
    private lateinit var ticketViewModel: TicketViewModel  // ViewModel para gestionar la lógica de la compra

    // Método que crea y retorna la vista del fragmento
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inicialización de EventDao para interactuar con la base de datos
        eventDao = ExpoManagerDatabase.getDatabase(requireContext()).eventDao() //Inicializamos eventDao

        // Configuración de DataBinding para vincular la vista XML
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_buy_ticket, container, false)

        // Verificación de argumentos recibidos (eventId y eventName)
        arguments?.let {
            eventId = it.getInt("eventId", -1) // ID del evento
            eventName = it.getString("eventName") // Nombre del evento
        }
        return binding.root // Retornar la vista enlazada
    }
    // Método que configura la lógica del fragmento una vez que la vista está creada
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Cargar el evento desde la base de datos en segundo plano (hilo IO)
        CoroutineScope(Dispatchers.IO).launch {
            val event = eventDao.getEventById(eventId) // Obtener el evento por su ID
            event?.let {
                eventRaffle = it // Guardar el evento en una variable
                withContext(Dispatchers.Main) {
                    // Mostrar el nombre e imagen del evento en la interfaz
                    binding.eventName.text = it.name
                    binding.eventImage.setImageResource(it.imageUrl)
                }
            }
        }

        // Configurar el ViewModel para gestionar la compra de tickets
        val apiService = (requireActivity().application as ExpoManagerApplication).getApiService()
        val ticketRepository = TicketRepository(apiService)


        ticketViewModel = ViewModelProvider(this, TicketViewModelFactory(ticketRepository))
            .get(TicketViewModel::class.java)

        // Observadores para manejar la respuesta de la compra de tickets
        ticketViewModel.ticket.observe(viewLifecycleOwner) { ticket ->
            // Mensaje de éxito si el ticket se genera correctamente
            Timber.i("Ticket generado(envio): $ticket")
            Toast.makeText(requireContext(), "Compra realizada: $ticket", Toast.LENGTH_SHORT).show()
        }

        ticketViewModel.error.observe(viewLifecycleOwner) { error ->
            // Mensaje de error si hay un problema en la compra
            Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_SHORT).show()
            Timber.e("Error: $error")
        }
        // Configuración del botón de confirmar compra
        binding.confirmButton.setOnClickListener {
            // Validar la cantidad de entradas ingresadas
            val ticketCount = binding.ticketCount.text.toString().toIntOrNull()
            if (ticketCount == null || ticketCount <= 0) {
                Toast.makeText(requireContext(), "Introduce al menos una entrada", Toast.LENGTH_SHORT).show()
            } else {
                // Simular proceso de compra
                binding.progressBar.visibility = View.VISIBLE
                binding.confirmButton.visibility = View.INVISIBLE

                // Enviar los datos del ticket a la API (POST)
                ticketViewModel.sendTicket(eventName ?: "Evento desconocido", ticketCount)

                /*ticketViewModel.ticket.observe(viewLifecycleOwner) { //ESperamos la respuesta del sendTicket
                    ticketViewModel.fetchTicket(1) //id genérico
                }*/
                // Simulación del tiempo de espera del pago (3.5 segundos)
                Handler(Looper.getMainLooper()).postDelayed({
                    // Ocultar la barra de progreso
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Compra realizada correctamente", Toast.LENGTH_SHORT).show()

                    // Navegar a RaffleActivity después de la compra
                    eventRaffle?.let {
                        val intent = Intent(requireContext(), RaffleActivity::class.java).apply {
                            putExtra("eventId", it.id)
                            putExtra("eventName", it.name)
                        }
                        startActivity(intent)
                    }
                }, 3500) //tiempo que tarda en "hacer" el pago de las entradas
                // Ocultar la interfaz de compra después de la simulación
                binding.ticketCountLayout.visibility= View.INVISIBLE
                // Mostrar imagen de éxito después de 5 segundos
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.ticketBuyCorrectImage.visibility = View.VISIBLE
                    binding.ticketBuyCorrectText.visibility = View.VISIBLE
                }, 5000)
            }
        }
        // Configurar el botón de regreso a la lista de eventos
        binding.buttonBackEvents.setOnClickListener{
            requireActivity().onBackPressedDispatcher.onBackPressed() // Regresar a la actividad anterior
        }
    }
}
