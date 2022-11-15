package com.android.busimap.fragmentos

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.busimap.R
import com.android.busimap.activities.HomeActivity
import com.android.busimap.activities.VistaCrearNegocio
import com.android.busimap.adapter.LugarAdapter
import com.android.busimap.bd.Lugares
import com.android.busimap.databinding.FragmentMisNegociosBinding
import com.android.busimap.modelo.Lugar
import com.android.busimap.sqlite.BusimapDbHelper
import com.android.busimap.util.EstadoConexion

class MisNegociosFragment : Fragment() {
    lateinit var binding: FragmentMisNegociosBinding
    var lista: ArrayList<Lugar> = ArrayList()
    lateinit var bd: BusimapDbHelper
    var estadoConexion: Boolean=false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMisNegociosBinding.inflate(inflater, container, false)


        binding.btnNuevoLugar.setOnClickListener { irACrearLugar() }

        val sp = requireActivity().getSharedPreferences("sesion", Context.MODE_PRIVATE)
        val codigoUsuario = sp.getInt("codigo_usuario", -1)

        if( codigoUsuario != -1 ){

            val estado = (requireActivity() as HomeActivity).estadoConexion

            lista = if (estado) {
                Lugares.listarPorPropietario(codigoUsuario)
            }else{
                bd.listarLugares().filter { l -> l.idCreador == codigoUsuario }.toCollection(ArrayList())
            }



            val adapter = LugarAdapter(lista)
            binding.listaMisLugares.adapter = adapter
            binding.listaMisLugares.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        }

        return binding.root
    }

    private fun irACrearLugar(){
        startActivity( Intent(activity, VistaCrearNegocio::class.java) )
    }


}