package com.android.busimap.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.busimap.R
import com.android.busimap.adapter.LugarAdapter
import com.android.busimap.bd.Lugares
import com.android.busimap.databinding.ActivityResultadoBusquedaBinding
import com.android.busimap.fragmentos.InicioFragment

import com.android.busimap.modelo.Lugar

class ResultadoBusqueda : AppCompatActivity() {

    lateinit var binding:ActivityResultadoBusquedaBinding
    var textoBusqueda:String = ""
    lateinit var listaLugares:ArrayList<Lugar>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado_busqueda)

        binding = ActivityResultadoBusquedaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textoBusqueda = intent.extras!!.getString("texto", "")
        listaLugares = ArrayList()

        if(textoBusqueda.isNotEmpty()){
            listaLugares = Lugares.buscarNombre(textoBusqueda)
        }

        supportFragmentManager.beginTransaction()
            .replace(binding.contenidoPrincipal.id, InicioFragment())
            .addToBackStack("Inicio fragment")
            .commit()

        val adapter = LugarAdapter(listaLugares)
        binding.listaLugares.adapter = adapter
        binding.listaLugares.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
}