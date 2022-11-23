package com.android.busimap.activities.detalleNegocio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.busimap.R
import com.android.busimap.adapter.CategoriasAdapter
import com.android.busimap.adapter.ImagenesViewPager
import com.android.busimap.adapter.LugarAdapter
import com.android.busimap.adapter.ViewPagerAdapter
import com.android.busimap.databinding.ActivityDetalleLugarBinding
import com.android.busimap.databinding.ActivityDetalleNegocioCategoriaBinding
import com.android.busimap.fragmentos.InicioFragment
import com.android.busimap.modelo.Categoria
import com.android.busimap.modelo.Lugar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DetalleNegocioCategoria : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleNegocioCategoriaBinding
    private var nombreCategoria:Int = 0
    var listaLugares:ArrayList<Lugar> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleNegocioCategoriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nombreCategoria = intent.extras!!.getInt("nombre",0)

        if(nombreCategoria != 0) {

            Firebase.firestore
                .collection("lugares")
                .whereEqualTo("idCategoria", nombreCategoria)
                .get()
                .addOnSuccessListener {
                    for (doc in it){
                        val lugar = doc.toObject(Lugar::class.java)
                        lugar.key = doc.id
                        listaLugares.add(lugar)

                        supportFragmentManager.beginTransaction()
                            .replace(binding.contenidoPrincipal.id, InicioFragment())
                            .addToBackStack("Inicio fragment")
                            .commit()

                        val adapter = LugarAdapter(listaLugares)
                        binding.listaLugares.adapter = adapter
                        binding.listaLugares.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

                        adapter.notifyItemInserted(listaLugares.size-1)
                    }
                }
        }
    }
}