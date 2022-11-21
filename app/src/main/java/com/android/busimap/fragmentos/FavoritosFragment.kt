package com.android.busimap.fragmentos

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.busimap.R
import com.android.busimap.adapter.ComentarioAdapter
import com.android.busimap.adapter.LugarAdapter
import com.android.busimap.bd.Lugares
import com.android.busimap.bd.Usuarios
import com.android.busimap.databinding.FragmentFavoritosBinding
import com.android.busimap.modelo.Comentario
import com.android.busimap.modelo.Favorito
import com.android.busimap.modelo.Lugar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class FavoritosFragment : Fragment() {
    lateinit var binding: FragmentFavoritosBinding
    var lista: ArrayList<Lugar> = ArrayList()
    var user: FirebaseUser? = null
    private var codigoLugar: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            codigoLugar = requireArguments().getString("id_lugar", "")
        }

        user = FirebaseAuth.getInstance().currentUser

        Firebase.firestore
            .collection("usuarios")
            .document(user!!.uid)
            .collection("favoritos")
            .get()
            .addOnSuccessListener {
                var favoritos = it.toObjects(Favorito::class.java) as ArrayList<Favorito>
                favoritos.forEach {
                        u->
                    Firebase.firestore
                        .collection("lugares")
                        .document(u.codigoLugar)
                        .get()
                        .addOnSuccessListener {
                                l->
                            val lugar = l.toObject(Lugar::class.java)

                            if (lugar != null) {
                                lugar.key = u.codigoLugar
                                lista.add(lugar)
                                val adapter = LugarAdapter(lista)
                                binding.listaMisLugares.adapter = adapter
                                binding.listaMisLugares.layoutManager =
                                    LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)


                                adapter.notifyItemInserted(lista.size-1)
                            }
                        }
                }
            }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritosBinding.inflate(inflater, container, false)

        return binding.root
    }

    companion object {

        fun newInstance(codigoLugar: String): FavoritosFragment {
            val args = Bundle()
            args.putString("id_lugar", codigoLugar)
            val fragmento = FavoritosFragment()
            fragmento.arguments = args
            return fragmento
        }

    }
}