package com.android.busimap.fragmentos

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.busimap.R
import com.android.busimap.activities.ResultadoBusqueda
import com.android.busimap.adapter.CategoriasAdapter
import com.android.busimap.adapter.LugarAdapter
import com.android.busimap.databinding.FragmentInicioBinding
import com.android.busimap.modelo.Categoria
import com.android.busimap.modelo.Lugar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class InicioFragment : Fragment() {
    lateinit var binding: FragmentInicioBinding
    var listaCategoria:ArrayList<Categoria> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInicioBinding.inflate(inflater, container, false)

        binding.textoBusqueda.setOnEditorActionListener { textView, i, keyEvent ->
            if( i == EditorInfo.IME_ACTION_SEARCH){

                val busqueda = binding.textoBusqueda.text.toString()

                if(busqueda.isNotEmpty()) {
                    val intent = Intent(activity, ResultadoBusqueda::class.java)
                    intent.putExtra("texto", busqueda)
                    startActivity(intent)
                }

            }
            true
        }

        Firebase.firestore
            .collection("categorias")
            .get()
            .addOnSuccessListener {
                for (doc in it){
                    val categoria = doc.toObject(Categoria::class.java)
                    categoria.key = doc.id
                    listaCategoria.add(categoria)

                    val adapter = CategoriasAdapter(listaCategoria)
                    binding.listaLugares.adapter = adapter
                    binding.listaLugares.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

                    adapter.notifyDataSetChanged()
                }
            }

        return binding.root
    }

}