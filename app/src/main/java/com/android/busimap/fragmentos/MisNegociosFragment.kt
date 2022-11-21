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
import android.widget.TextView
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
import com.android.busimap.modelo.Usuario
import com.android.busimap.sqlite.BusimapDbHelper
import com.android.busimap.util.EstadoConexion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class MisNegociosFragment : Fragment() {
    lateinit var binding: FragmentMisNegociosBinding
    var lista: ArrayList<Lugar> = ArrayList()
    lateinit var bd: BusimapDbHelper
    var estadoConexion: Boolean=false
    lateinit var adapter:LugarAdapter
    var user: FirebaseUser?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMisNegociosBinding.inflate(inflater, container, false)

        binding.btnNuevoLugar.setOnClickListener { irACrearLugar() }

        user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            lista= ArrayList()
            adapter = LugarAdapter(lista)
            binding.listaMisLugares.adapter = adapter
            binding.listaMisLugares.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        }


        return binding.root
    }

    override fun onResume(){
        super.onResume()
        lista.clear()
        Firebase.firestore
            .collection("lugares")
            .whereEqualTo("idCreador",user?.uid)
            .get()
            .addOnSuccessListener {
                for (doc in it){
                    val lugar = doc.toObject(Lugar::class.java)
                    lugar.key = doc.id
                    lista.add(lugar)
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun irACrearLugar(){
        startActivity( Intent(activity, VistaCrearNegocio::class.java) )
    }


}