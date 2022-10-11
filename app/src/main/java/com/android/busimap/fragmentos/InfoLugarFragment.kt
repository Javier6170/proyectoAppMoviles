package com.android.busimap.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.android.busimap.R
import com.android.busimap.bd.Categorias
import com.android.busimap.bd.Comentarios
import com.android.busimap.bd.Lugares
import com.android.busimap.databinding.FragmentInfoLugarBinding
import com.android.busimap.modelo.Lugar


class InfoLugarFragment : Fragment() {

    lateinit var binding:FragmentInfoLugarBinding
    private var lugar: Lugar? = null
    private var codigoLugar:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if(arguments != null){
            codigoLugar = requireArguments().getInt("id_lugar")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoLugarBinding.inflate(inflater, container, false)

        lugar = Lugares.obtener(codigoLugar)

        if(lugar != null) {
            cargarInformacion(lugar!!)
            dibujarEstrellas(lugar!!)
        }

        return binding.root
    }


    fun cargarInformacion(lugar:Lugar){

        binding.nombreLugar.text = lugar.nombre
        binding.descripcionLugar.text = lugar.descripcion
        binding.direccionLugar.text = lugar.direccion
        binding.iconoCategoria.text = Categorias.obtener(lugar.idCategoria)!!.icono

        var telefonos = ""

        if(lugar.telefonos.isNotEmpty()) {
            for (tel in lugar.telefonos) {
                telefonos += "$tel, "
            }
            telefonos = telefonos.substring(0, telefonos.length - 2)
        }else{
            telefonos = "No hay teléfono"
        }

        binding.telefonoLugar.text = telefonos

        var horarios = ""

        for( horario in lugar.horarios ){
            for(dia in horario.diaSemana){
                horarios += "${dia.toString().lowercase().replaceFirstChar { it.uppercase() }}: ${horario.horaInicio}:00 - ${horario.horaCierre}:00\n"
            }
        }

        binding.horariosLugar.text = horarios

    }

    fun dibujarEstrellas(lugar:Lugar){

        val calificacion = lugar.obtenerCalificacionPromedio( Comentarios.listar(lugar.id) )

        for( i in 0..calificacion ){
            (binding.estrellas.lista[i] as TextView).setTextColor( ContextCompat.getColor(requireContext(), R.color.yellow) )
        }

    }

    companion object {
        fun newInstance(codigoLugar:Int):InfoLugarFragment{
            val args = Bundle()
            args.putInt("id_lugar", codigoLugar)
            val fragmento = InfoLugarFragment()
            fragmento.arguments = args
            return fragmento
        }
    }
}