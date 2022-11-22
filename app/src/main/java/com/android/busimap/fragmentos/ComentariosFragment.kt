package com.android.busimap.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.busimap.R
import com.android.busimap.adapter.ComentarioAdapter
import com.android.busimap.bd.Comentarios
import com.android.busimap.databinding.FragmentComentariosBinding
import com.android.busimap.modelo.Comentario
import com.android.busimap.modelo.Favorito
import com.android.busimap.modelo.Lugar
import com.android.busimap.modelo.Usuario
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class ComentariosFragment : Fragment() {

    lateinit var binding: FragmentComentariosBinding
    var lista: ArrayList<Comentario> = ArrayList()
    private var codigoLugar: String = ""
    private lateinit var adapter: ComentarioAdapter
    private var esFavorito = false

    private var colorPorDefecto: Int = 0
    private var colorPorDefectoCorazones: Int = 0
    private val SHORT_DURATION_MS = 4500
    private var estrellas = 0
    private var corazones = 0

    var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            codigoLugar = requireArguments().getString("id_lugar", "")
        }

        user = FirebaseAuth.getInstance().currentUser

        Firebase
            .firestore
            .collection("usuarios")
            .document(user!!.uid)
            .collection("favoritos")
            .whereEqualTo("codigoLugar", codigoLugar)
            .get()
            .addOnSuccessListener {
                var favorito = it.toObjects(Favorito::class.java)
                favorito.forEach { u ->
                    Firebase.firestore
                        .collection("lugares")
                        .document(u.codigoLugar)
                        .get()
                        .addOnSuccessListener { l ->
                            var lugar = l.toObject(Lugar::class.java)
                            if (lugar!=null){
                                esFavorito = true
                                val pos = 0
                                estrellas = pos + 1
                                for (i in 0..pos) {
                                    (binding.corazones.listaCorazones[i] as TextView).setTextColor(
                                        ContextCompat.getColor(
                                            requireContext(),
                                            R.color.red
                                        )
                                    )
                                }
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
        binding = FragmentComentariosBinding.inflate(inflater, container, false)

        colorPorDefecto = binding.estrellas.e1.textColors.defaultColor
        colorPorDefectoCorazones = binding.corazones.e1.textColors.defaultColor


        Firebase.firestore
            .collection("lugares")
            .document(codigoLugar)
            .collection("comentarios")
            .get()
            .addOnSuccessListener {
                for (doc in it) {
                    val comentario = doc.toObject(Comentario::class.java)
                    comentario.key = doc.id
                    lista.add(comentario)


                    adapter = ComentarioAdapter(lista)
                    binding.listaComentarios.adapter = adapter
                    binding.listaComentarios.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    adapter.notifyItemInserted(lista.size - 1)
                }
            }
            .addOnFailureListener {
                Snackbar.make(binding.root, "${it.message}", Snackbar.LENGTH_LONG).show()
            }


        binding.comentarLugar.setOnClickListener { hacerComentario() }

        for (i in 0 until binding.estrellas.lista.childCount) {
            (binding.estrellas.lista[i] as TextView).setOnClickListener { presionarEstrella(i) }
        }

        for (i in 0 until binding.corazones.listaCorazones.childCount) {
            (binding.corazones.listaCorazones[i] as TextView).setOnClickListener { hacerFavorito(i) }
        }



        return binding.root
    }

    fun hacerComentario() {

        val texto = binding.mensajeComentario.text.toString()

        user = FirebaseAuth.getInstance().currentUser

        if (texto.isNotEmpty() && estrellas > 0) {
            if (user != null) {
                val comentario = Comentario(texto, user!!.uid, estrellas)
                Firebase.firestore
                    .collection("lugares")
                    .document(codigoLugar)
                    .collection("comentarios")
                    .add(comentario)
                    .addOnSuccessListener {
                        binding.txtSinComentarios.visibility = View.GONE

                        limpiarFormulario()
                        Snackbar.make(
                            binding.root,
                            getString(R.string.comentario_realizado),
                            Snackbar.LENGTH_LONG
                        ).show()

                        lista.add(comentario)
                        adapter.notifyItemInserted(lista.size - 1)
                    }
                    .addOnFailureListener {
                        Snackbar.make(binding.root, "${it.message}", Snackbar.LENGTH_LONG).show()
                    }
            }
        } else {
            Snackbar.make(binding.root, getString(R.string.comentario_error), Snackbar.LENGTH_LONG)
                .show()
        }
    }

    private fun limpiarFormulario() {
        binding.mensajeComentario.setText("")
        borrarSeleccion()
        borrarSeleccionCorazones()
        corazones = 0
        estrellas = 0
    }

    private fun presionarEstrella(pos: Int) {
        estrellas = pos + 1
        borrarSeleccion()
        for (i in 0..pos) {
            (binding.estrellas.lista[i] as TextView).setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.yellow
                )
            )
        }
    }

    private fun hacerFavorito(pos: Int) {
        if (!esFavorito){
                for (i in 0..pos) {
                    (binding.corazones.listaCorazones[i] as TextView).setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )
                }
                if (user != null) {
                    var favorito = Favorito(codigoLugar)
                    Firebase.firestore
                        .collection("usuarios")
                        .document(user!!.uid)
                        .collection("favoritos")
                        .document(codigoLugar)
                        .set(favorito)
                        .addOnSuccessListener {
                            Snackbar.make(
                                binding.root,
                                "Se ha convertido en tu lugar favorito",
                                SHORT_DURATION_MS
                            ).show()
                        }
                }
        }else{
            borrarSeleccionCorazones()
            user = FirebaseAuth.getInstance().currentUser
            Firebase.firestore
                .collection("usuarios")
                .document(user!!.uid)
                .collection("favoritos")
                .document(codigoLugar)
                .delete()
                .addOnSuccessListener {
                    Snackbar.make(
                        binding.root,
                        "Hemos eliminado de tus lugares favoritos",
                        SHORT_DURATION_MS
                    ).show()

                }

        }
    }

    private fun borrarSeleccionCorazones() {
        (binding.corazones.listaCorazones[0] as TextView).setTextColor(colorPorDefectoCorazones)
    }

    private fun borrarSeleccion() {
        for (i in 0 until binding.estrellas.lista.childCount) {
            (binding.estrellas.lista[i] as TextView).setTextColor(colorPorDefecto)
        }
    }

    companion object {

        fun newInstance(codigoLugar: String): ComentariosFragment {
            val args = Bundle()
            args.putString("id_lugar", codigoLugar)
            val fragmento = ComentariosFragment()
            fragmento.arguments = args
            return fragmento
        }

    }


}


