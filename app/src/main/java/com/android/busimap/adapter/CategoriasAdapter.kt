package com.android.busimap.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.busimap.activities.detalleNegocio.DetalleNegocioCategoria
import com.android.busimap.databinding.CategoriasLugarBinding
import com.android.busimap.modelo.Categoria
import com.android.busimap.modelo.Lugar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class CategoriasAdapter(private var lista: ArrayList<Categoria>) :
    RecyclerView.Adapter<CategoriasAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = CategoriasLugarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CategoriasAdapter.ViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    inner class ViewHolder(private var view: CategoriasLugarBinding) : RecyclerView.ViewHolder(view.root),
        View.OnClickListener {

        private var nombreCategoria: Int = 0

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(lugar: Categoria) {
            view.btnCategoria.text = lugar.nombre
            nombreCategoria = lugar.id

            view.btnCategoria.setOnClickListener {
                onClick(view.root)
            }
        }


        override fun onClick(p0: View?) {
            val intent = Intent(view.root.context, DetalleNegocioCategoria::class.java)
            intent.putExtra("nombre", nombreCategoria)
            view.root.context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int = lista.size
}