package com.android.busimap.fragmentos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.busimap.R
import com.android.busimap.activities.VistaCrearNegocio
import com.android.busimap.adapter.LugarAdapter
import com.android.busimap.bd.Lugares
import com.android.busimap.databinding.FragmentMisNegociosBinding
import com.android.busimap.modelo.Lugar

class MisNegociosFragment : Fragment() {
    lateinit var binding: FragmentMisNegociosBinding
    var lista: ArrayList<Lugar> = ArrayList()

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
            lista = Lugares.listarPorPropietario(codigoUsuario)

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