package com.android.busimap.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.busimap.R
import com.android.busimap.adapter.LugarAdapter
import com.android.busimap.bd.Lugares
import com.android.busimap.databinding.FragmentAceptadosModeradorBinding
import com.android.busimap.databinding.FragmentDenegadosModeradorBinding
import com.android.busimap.modelo.EstadoLugar
import com.android.busimap.modelo.Lugar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class DenegadosModeradorFragment : Fragment() {
    lateinit var binding: FragmentDenegadosModeradorBinding
    var listaLugares: ArrayList<Lugar> = ArrayList()
    lateinit var adapterLista: LugarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDenegadosModeradorBinding.inflate(inflater, container, false)
        adapterLista = LugarAdapter(listaLugares)
        binding.listaLugaresDenegados.adapter = adapterLista
        binding.listaLugaresDenegados.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        return binding.root
    }

    companion object {

    }

    override fun onResume(){
        super.onResume()
        listaLugares.clear()
        Firebase.firestore
            .collection("lugares")
            .whereEqualTo("estado","RECHAZADO")
            .get()
            .addOnSuccessListener {
                for (doc in it){
                    val lugar = doc.toObject(Lugar::class.java)
                    lugar.key = doc.id
                    listaLugares.add(lugar)
                }
                adapterLista.notifyDataSetChanged()
            }
    }
}