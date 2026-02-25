package com.example.expomanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.expomanager.databinding.FragmentRaffleSecondBinding

/** Clase RaffleSecondFragment que extiende Fragment
 * Gestiona la interfaz de la segunda pantalla de la rifa
 */
class RaffleSecondFragment : Fragment(){
    // Variable de DataBinding para enlazar la interfaz de usuario (XML) con este fragmento
    private lateinit var binding: FragmentRaffleSecondBinding
    private lateinit var viewModel: RaffleViewModel //comparte el view model con todos los fragmentos que lo usen

    /**
     * Método que crea y retorna la vista del fragmento
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inicialización de DataBinding
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_raffle_second,// Diseño asociado (fragment_raffle_second.xml)
            container,
            false
        )
        return binding.root
    }

    /**
     *  Método que configura la lógica del fragmento una vez que la vista está creada
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Conectamos el fragment con mainviewmodel -> para que main activity y fragment2 -> usen mismo objeto
        viewModel = ViewModelProvider(requireActivity()).get(RaffleViewModel::class.java)

        // Elementos visuales del fragmento
        val couponRaffle = requireActivity().findViewById<ImageView>(R.id.raffle_frag2_winner)
        val couponValue = requireActivity().findViewById<TextView>(R.id.raffle_coupon_value)
        //Usamos el isRaffleWinner para observar
        // Observamos el estado de la rifa (si es ganador o no)
        viewModel.isRaffleWinner.observe(viewLifecycleOwner) {
            if (it == true) {
                // Mensaje de ganador
                binding.textView.text = getString(R.string.raffle_winner_msg)
                // Animación de aparición del cupón
                val anim = AlphaAnimation(0f, 1f).apply { // Animación del cupón
                    duration = 500 // Duración de la animación en milisegundos
                    fillAfter = true // Mantener la animación final
                }
                couponRaffle.startAnimation(anim)
                couponRaffle.visibility = VISIBLE
                // Generamos un código aleatorio para el cupón
                couponValue.text = viewModel.generateRandomString(6)
                //Guardar el cupón en la base de datos
                val code = couponValue.text.toString()
                val eventId = requireActivity().intent.getIntExtra("eventId", -1)
                val eventName = requireActivity().intent.getStringExtra("eventName") ?: "Error nombre no disponible"
                viewModel.saveCouponDBwinner(eventId, eventName, code)
            }
            else{
                // Mensaje de perdedor
                binding.textView.text = getString(R.string.raffle_loser_msg)
                couponRaffle.visibility = INVISIBLE
            }
        }
    }
}