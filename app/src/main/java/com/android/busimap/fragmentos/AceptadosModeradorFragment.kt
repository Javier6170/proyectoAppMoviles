package com.android.busimap.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.busimap.R
import com.android.busimap.adapter.LugarAdapter
import com.android.busimap.bd.Lugares
import com.android.busimap.databinding.FragmentAceptadosModeradorBinding
import com.android.busimap.databinding.FragmentListaLugaresModeradorBinding
import com.android.busimap.modelo.EstadoLugar
import com.android.busimap.modelo.Lugar


class AceptadosModeradorFragment : Fragment() {


    lateinit var binding: FragmentAceptadosModeradorBinding
    lateinit var listaLugares: ArrayList<Lugar>
    lateinit var adapterLista: LugarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listaLugares = Lugares.listarPorEstado(EstadoLugar.ACEPTADO)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAceptadosModeradorBinding.inflate(inflater, container, false)
        adapterLista = LugarAdapter(listaLugares)
        binding.listaLugaresAceptados.adapter = adapterLista
        binding.listaLugaresAceptados.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        return binding.root
    }

    companion object {

    }
}