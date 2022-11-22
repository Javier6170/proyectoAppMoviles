package com.android.busimap.fragmentos

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.android.busimap.activities.HomeActivityModerador
import com.android.busimap.databinding.FragmentEliminarModeradorBinding
import com.android.busimap.modelo.Lugar
import com.android.busimap.modelo.Rol
import com.android.busimap.modelo.Usuario
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class EliminarModeradorFragment : Fragment() {

    lateinit var binding: FragmentEliminarModeradorBinding
    var posModerador: Int = -1
    private val SHORT_DURATION_MS = 4500

    var moderadores: ArrayList<Usuario> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.firestore
            .collection("usuarios")
            .get()
            .addOnSuccessListener {
                var usuarios = it.toObjects(Usuario::class.java)
                usuarios.forEach { u ->
                    val rol = u.rol
                    if (rol == Rol.MODERADOR) {
                        moderadores.add(u)
                    }

                }
            }

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
        val idModerador = moderadores[posModerador].key

        Firebase.firestore
            .collection("usuarios")
            .document(idModerador)
            .delete()
            .addOnSuccessListener {
                Snackbar.make(binding.root, "Persona eliminada con exito", SHORT_DURATION_MS).show()
            }.addOnFailureListener {
                Snackbar.make(binding.root, "Persona no existe", SHORT_DURATION_MS).show()
            }
    }

    override fun onResume() {
        super.onResume()

    }

    companion object {

    }
}