package com.android.busimap.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.android.busimap.R
import com.android.busimap.bd.Categorias
import com.android.busimap.bd.Ciudades
import com.android.busimap.bd.Horarios
import com.android.busimap.bd.Lugares
import com.android.busimap.databinding.ActivityVistaCrearNegocioBinding
import com.android.busimap.fragmentos.HorariosDialogoFragment
import com.android.busimap.modelo.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

class VistaCrearNegocio : AppCompatActivity(), HorariosDialogoFragment.OnHorarioCreadoListener,
    OnMapReadyCallback {

    lateinit var binding: ActivityVistaCrearNegocioBinding
    var posCiudad:Int = -1
    var posCategoria:Int = -1
    lateinit var ciudades:ArrayList<Ciudad>
    lateinit var categorias:ArrayList<Categoria>
    lateinit var horarios:ArrayList<Horario>
    lateinit var gMap:GoogleMap
    private var tienePermiso = false
    private val defaultLocation = LatLng(4.550923, -75.6557201)
    private var posicion:Posicion? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVistaCrearNegocioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        horarios = ArrayList()
        ciudades = Ciudades.listar()
        categorias = Categorias.listar()

        cargarCiudades()
        cargarCategorias()
        binding.btnCrearLugar.setOnClickListener { crearNuevoLugar() }

        binding.btnAsignarHorario.setOnClickListener{ mostrarDialogo()}


        val mapFragment = supportFragmentManager.findFragmentById( R.id.mapa_crear_lugar ) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    fun mostrarDialogo(){

        val dialog = HorariosDialogoFragment()
        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogoTitulo)
        dialog.listener = this
        dialog.show(supportFragmentManager, "Agregar")

    }

    fun cargarCiudades(){

        var lista = ciudades.map { c -> c.nombre }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, lista)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.ciudadLugar.adapter = adapter

        binding.ciudadLugar.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                posCiudad = p2
            }

            override fun onNothingSelected(p0: AdapterView<*>?) { }
        }
    }

    fun cargarCategorias(){
        var lista = categorias.map { c -> c.nombre }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, lista)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categoriaLugar.adapter = adapter

        binding.categoriaLugar.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                posCategoria = p2
            }

            override fun onNothingSelected(p0: AdapterView<*>?) { }
        }
    }

    fun crearNuevoLugar(){

        val nombre = binding.nombreLugar.text.toString()
        val descripcion = binding.descripcionLugar.text.toString()
        val telefono = binding.telefonoLugar.text.toString()
        val direccion = binding.direccionLugar.text.toString()
        val idCiudad = ciudades[posCiudad].id
        val idCategoria = categorias[posCategoria].id

        if( nombre.isEmpty() ){
            binding.nombreLayout.error = getString(R.string.es_obligatorio)
        }else{
            binding.nombreLayout.error = null
        }

        if( descripcion.isEmpty() ){
            binding.descripcionLayout.error = getString(R.string.es_obligatorio)
        }else{
            binding.descripcionLayout.error = null
        }

        if( direccion.isEmpty() ){
            binding.direccionLayout.error = getString(R.string.es_obligatorio)
        }else{
            binding.direccionLayout.error = null
        }

        if( telefono.isEmpty() ){
            binding.telefonoLayout.error = getString(R.string.es_obligatorio)
        }else{
            binding.telefonoLayout.error = null
        }

        if(nombre.isNotEmpty() && descripcion.isNotEmpty() && telefono.isNotEmpty() && direccion.isNotEmpty() && horarios.isNotEmpty() && idCiudad != -1 && idCategoria != -1)  {

            if(posicion!=null) {

                val nuevoLugar = Lugar(
                    nombre,
                    descripcion,
                    1,
                    EstadoLugar.SIN_REVISAR,
                    idCategoria,
                    direccion,
                    posicion!!,
                    idCiudad
                )

                val telefonos: ArrayList<String> = ArrayList()
                telefonos.add(telefono)

                nuevoLugar.telefonos = telefonos
                nuevoLugar.horarios = horarios

                Lugares.crear(nuevoLugar)

                Snackbar.make(binding.root, getString(R.string.lugar_creado), Toast.LENGTH_LONG)
                    .show()

            }else{
                Snackbar.make(binding.root, getString(R.string.mensaje_mapa), Toast.LENGTH_LONG)
                    .show()
            }
        }else{
            Snackbar.make(binding.root, getString(R.string.todos_obligatorios), Toast.LENGTH_LONG)
                .show()
        }

    }

    override fun elegirHorario(horario: Horario) {
        horario.diaSemana.forEach{
            val textView = TextView(this)
            textView.text = "${it.name.lowercase().replaceFirstChar { c -> c.uppercase() }}  ${horario.horaInicio}:00 - ${horario.horaCierre}:00 "
            binding.listaHorarios.addView(textView)
        }

        binding.espacioHorarios.visibility = View.VISIBLE

        horarios.add(horario)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        gMap.uiSettings.isZoomControlsEnabled = true

        gMap.moveCamera( CameraUpdateFactory.newLatLngZoom( defaultLocation, 12f ) )

        gMap.setOnMapClickListener {

            if(posicion==null){
                posicion = Posicion()
            }

            posicion!!.lat = it.latitude
            posicion!!.lng = it.longitude

            gMap.clear()
            gMap.addMarker( MarkerOptions().position(it) )
        }
    }

}