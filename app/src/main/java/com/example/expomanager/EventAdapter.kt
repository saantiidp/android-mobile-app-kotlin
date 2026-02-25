package com.example.expomanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expomanager.databinding.ListItemEventBinding

// Definición de la clase EventAdapter que extiende RecyclerView.Adapter
// Este adaptador gestiona la lista de eventos que se muestran en un RecyclerView
class EventAdapter(private val onEventClick: (Event) -> Unit) : RecyclerView.Adapter<EventAdapter.EventHolder>() {

    // Variable para almacenar el enlace (binding) de la vista
    private lateinit var binding: ListItemEventBinding
    // Lista de eventos que se mostrarán en el RecyclerView
    var events = listOf<Event>()
    // Clase interna que gestiona la vista de cada elemento (ViewHolder)
    inner class EventHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Utilizamos binding local para acceder a los elementos del diseño XML
        private val localBinding = binding

        // Método para enlazar los datos del evento a la interfaz (ListItemEvent)
        fun bind(event: Event) {
            // Asignamos el evento al binding (esto permite usar DataBinding en el XML)
            localBinding.event = event
            // Configuramos la imagen del evento (imageViewEvent) usando su URL de imagen
            localBinding.imageViewEvent.setImageResource(event.imageUrl)
            // Configuración del clic en el elemento completo del evento
            itemView.setOnClickListener {
                onEventClick(event)  // Llamamos a la función de clic que recibimos como parámetro
            }
            // Configuración del clic en el botón específico del evento (buttonEvent)
            localBinding.buttonEvent.setOnClickListener {
                onEventClick(event)  // Llamamos a la misma función para ver detalles
            }
        }
    }
    // Método que crea la vista para cada elemento (ViewHolder)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        // Inflamos el diseño XML (ListItemEvent) y lo vinculamos a este adaptador
        val inflater = LayoutInflater.from(parent.context)
        binding = ListItemEventBinding.inflate(inflater, parent, false)
        return EventHolder(binding.root)
    }

    //Comienza a reutilizar el holder creado, ocultando los elementos previos
    //contenedor recien creado o reciclado y el indice del elemento a mostrar
    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        holder.bind(events[position])// Llamamos al método bind para cada evento
    }
    // Método que indica la cantidad de elementos en la lista
    override fun getItemCount(): Int {
        return events.size
    }
}
