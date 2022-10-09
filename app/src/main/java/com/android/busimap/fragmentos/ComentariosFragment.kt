package com.android.busimap.fragmentos

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.busimap.R
import com.android.busimap.adapter.ComentarioAdapter
import com.android.busimap.bd.Comentarios
import com.android.busimap.databinding.FragmentComentariosBinding
import com.android.busimap.modelo.Comentario
import com.google.android.material.snackbar.Snackbar


class ComentariosFragment : Fragment() {

    lateinit var binding:FragmentComentariosBinding
    var lista:ArrayList<Comentario> = ArrayList()
    private var codigoLugar:Int = 0
    private lateinit var adapter: ComentarioAdapter
    var codigoUsuario:Int = 0
    private var colorPorDefecto: Int = 0
    private var estrellas = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sp = requireActivity().getSharedPreferences("sesion", Context.MODE_PRIVATE)
        codigoUsuario = sp.getInt("codigo_usuario", -1)

        if(arguments != null){
            codigoLugar = requireArguments().getInt("id_lugar")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentComentariosBinding.inflate(inflater, container, false)

        colorPorDefecto = binding.estrellas.e1.textColors.defaultColor

        lista = Comentarios.listar(codigoLugar)
        adapter = ComentarioAdapter(lista)
        binding.listaComentarios.adapter = adapter
        binding.listaComentarios.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.comentarLugar.setOnClickListener { hacerComentario() }
/*
        for ( i in 0 until binding.estrellas.lista.childCount){
            (binding.estrellas.lista[i] as TextView).setOnClickListener { presionarEstrella(i) }
        }
*/
        return binding.root
    }
    fun hacerComentario(){

        val texto = binding.mensajeComentario.text.toString()

        if( texto.isNotEmpty() && estrellas > 0){
            val comentario = Comentarios.crear( Comentario(texto, codigoUsuario, codigoLugar, estrellas) )

            limpiarFormulario()
            Snackbar.make(binding.root, getString(R.string.comentario_realizado), Snackbar.LENGTH_LONG ).show()
/*
            lista.add(comentario)
            */

            adapter.notifyItemInserted(lista.size-1)

        }else{
            Snackbar.make(binding.root, getString(R.string.comentario_error), Snackbar.LENGTH_LONG ).show()
        }

    }

    private fun limpiarFormulario(){
        binding.mensajeComentario.setText("")
        borrarSeleccion()
        estrellas = 0
    }

    private fun presionarEstrella(pos:Int){
        estrellas = pos+1
        borrarSeleccion()
        /*
        for( i in 0..pos ){
            (binding.estrellas.lista[i] as TextView).setTextColor( ContextCompat.getColor(requireContext(), R.color.yellow) )
        }

         */
    }

    private fun borrarSeleccion(){
        /*
        for ( i in 0 until binding.estrellas.lista.childCount){
            (binding.estrellas.lista[i] as TextView).setTextColor( colorPorDefecto )
        }

         */
    }

    companion object{

        fun newInstance(codigoLugar:Int):ComentariosFragment{
            val args = Bundle()
            args.putInt("id_lugar", codigoLugar)
            val fragmento = ComentariosFragment()
            fragmento.arguments = args
            return fragmento
        }

    }


}


