package com.android.busimap.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.android.busimap.activities.DetalleLugarActivity
import com.android.busimap.modelo.Lugar
import com.android.busimap.R
import com.android.busimap.databinding.ItemLugarBinding
import com.android.busimap.modelo.Categoria
import com.android.busimap.modelo.Comentario
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LugarAdapter(private var lista: ArrayList<Lugar>) :
    RecyclerView.Adapter<LugarAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemLugarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    override fun getItemCount() = lista.size

    inner class ViewHolder(private var view: ItemLugarBinding) : RecyclerView.ViewHolder(view.root),
        View.OnClickListener {

        private var codigoLugar: String = ""

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(lugar: Lugar) {
            view.nombreLugar.text = lugar.nombre
            view.direccionLugar.text = lugar.direccion

            codigoLugar = lugar.key

            val estaAbierto = lugar.estaAbierto()

            if (estaAbierto) {
                view.estadoLugar.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.color_green_light
                    )
                )
                view.horarioLugar.text = "Cierra a las ${lugar.obtenerHoraCierre()}"
            } else {
                view.estadoLugar.setTextColor(ContextCompat.getColor(itemView.context, R.color.red))
                try {
                    view.horarioLugar.text = "Abre el ${lugar.obtenerHoraApertura()}"
                } catch (e: Exception) {
                }
            }

            var comentarios: ArrayList<Comentario> = ArrayList()

            Firebase.firestore
                .collection("comentarios")
                .get()
                .addOnSuccessListener {
                    comentarios = it.toObjects(Comentario::class.java) as ArrayList<Comentario>
                }

            Firebase.firestore
                .collection("lugares")
                .document(codigoLugar)
                .get()
                .addOnSuccessListener {
                    var lugarF = it.toObject(Lugar::class.java)
                    if (lugarF != null) {
                        val calificacion = lugarF.obtenerCalificacionPromedio(comentarios)
                        for (i in 0..calificacion) {
                            (view.listaEstrellas[i] as TextView).setTextColor(
                                ContextCompat.getColor(
                                    view.listaEstrellas.context,
                                    R.color.yellow
                                )
                            )
                        }
                        Glide.with(view.root.context)
                            .load(lugarF.imagenes.first())
                            .into(view.imgLugar)
                    }

                }
            Firebase.firestore
                .collection("categorias")
                .whereEqualTo("id", lugar.idCategoria)
                .get()
                .addOnSuccessListener {
                    for (dec in it) {
                        view.iconoLugar.text = dec.toObject(Categoria::class.java).icono
                    }

                }
            view.estadoLugar.text = if (estaAbierto) {
                view.estadoLugar.context.getString(R.string.abierto)
            } else {
                view.estadoLugar.context.getString(R.string.cerrado)
            }


        }


        override fun onClick(p0: View?) {
            val intent = Intent(view.root.context, DetalleLugarActivity::class.java)
            intent.putExtra("codigo", codigoLugar)
            view.root.context.startActivity(intent)
        }


    }

}