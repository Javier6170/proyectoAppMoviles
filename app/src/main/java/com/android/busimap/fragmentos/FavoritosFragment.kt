package com.android.busimap.fragmentos

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.busimap.R
import com.android.busimap.adapter.LugarAdapter
import com.android.busimap.bd.Lugares
import com.android.busimap.bd.Usuarios
import com.android.busimap.databinding.FragmentFavoritosBinding
import com.android.busimap.modelo.Lugar


class FavoritosFragment : Fragment() {
    lateinit var binding: FragmentFavoritosBinding
    var lista: ArrayList<Lugar> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritosBinding.inflate(inflater, container, false)

        val sp = requireActivity().getSharedPreferences("sesion", Context.MODE_PRIVATE)
        val codigoUsuario = sp.getInt("codigo_usuario", -1)

        if (codigoUsuario != -1) {
            lista = Usuarios.obtener(codigoUsuario)!!.lugaresFavoritos
            if (lista.size != 0) {
                val adapter = LugarAdapter(lista)
                binding.listaMisLugares.adapter = adapter
                binding.listaMisLugares.layoutManager =
                    LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            }

        }
        return binding.root
    }
}