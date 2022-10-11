package com.android.busimap.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.busimap.R
import com.android.busimap.bd.Moderadores
import com.android.busimap.databinding.FragmentDenegadosModeradorBinding
import com.android.busimap.databinding.FragmentEliminarModeradorBinding
import com.android.busimap.modelo.Ciudad
import com.android.busimap.modelo.Moderador
import com.google.android.material.snackbar.Snackbar


class EliminarModeradorFragment : Fragment() {

    lateinit var binding: FragmentEliminarModeradorBinding
    var posModerador: Int = -1
    private val SHORT_DURATION_MS = 4500

    lateinit var moderadores: ArrayList<Moderador>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moderadores = Moderadores.listar()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEliminarModeradorBinding.inflate(inflater, container, false)
        cargarModeradores()
        binding.btnEliminarMode.setOnClickListener { eliminarMode() }
        return binding.root
    }

    fun cargarModeradores() {
        var lista = moderadores.map { c -> c.nombre }
        val adapter =
            activity?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, lista) }
        adapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.listaModeradores.adapter = adapter

        binding.listaModeradores.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    posModerador = p2
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
    }

    fun eliminarMode() {
        val idModerador = moderadores[posModerador].id

        var mode: Moderador? = Moderadores.obtener(idModerador)

        if (mode != null) {
            Moderadores.eliminar(mode)
            Snackbar.make(binding.root, "Persona eliminada con exito", SHORT_DURATION_MS).show()
        } else {
            Snackbar.make(binding.root, "Persona no existe", SHORT_DURATION_MS).show()
        }
    }

    companion object {

    }
}