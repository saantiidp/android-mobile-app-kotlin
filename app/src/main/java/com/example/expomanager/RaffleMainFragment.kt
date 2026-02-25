package com.example.expomanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.expomanager.databinding.FragmentRaffleMainBinding

// Clase RaffleMainFragment que extiende Fragment
// Gestiona la pantalla principal de la rifa, donde se muestra el número generado
class RaffleMainFragment : Fragment(){
    // Variable de DataBinding para enlazar la interfaz de usuario (XML) con este fragmento
    private lateinit var binding: FragmentRaffleMainBinding
    private lateinit var viewModel: RaffleViewModel //comparte el view model con todos los frag q lo usen
    // Método que crea y retorna la vista del fragmento
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inicialización de DataBinding
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_raffle_main,// Diseño asociado (fragment_raffle_main.xml)
            container,
            false
        )

        return binding.root // Retorna la vista configurada
    }
    // Método que configura la lógica del fragmento una vez que la vista está creada
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Conectamos el fragment con mainviewmodel -> para q main activity y fragm -> usen mismo obj
        viewModel = ViewModelProvider(requireActivity()).get(RaffleViewModel::class.java)

        // Observador para mostrar el número generado en el TextView
        val observer = Observer<Int> {
            binding.textView.text = it.toString()
        }
        // El TextView observa el número del ViewModel
        viewModel.number.observe(viewLifecycleOwner, observer) // con lifecycle --> solo si la actividad esta viva
    }
}