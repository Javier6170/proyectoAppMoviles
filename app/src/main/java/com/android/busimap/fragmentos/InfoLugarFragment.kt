package com.android.busimap.fragmentos

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.get
import com.android.busimap.R
import com.android.busimap.activities.HomeActivity
import com.android.busimap.bd.Categorias
import com.android.busimap.bd.Comentarios
import com.android.busimap.bd.Lugares
import com.android.busimap.databinding.FragmentInfoLugarBinding

import com.android.busimap.modelo.Categoria
import com.android.busimap.modelo.Comentario
import com.android.busimap.modelo.EstadoLugar
import com.android.busimap.modelo.Lugar
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase


class InfoLugarFragment : Fragment(), OnMapReadyCallback {

    lateinit var binding:FragmentInfoLugarBinding
    private var codigoLugar:String = ""
    private var typefaceSolid:Typeface? = null
    private var typefaceRegular:Typeface? = null
    var user: FirebaseUser? = null
    var listaLugares: ArrayList<Lugar> = ArrayList()
    lateinit var gMap: GoogleMap
    private val defaultLocation = LatLng(4.550923, -75.6557201)
    private var tienePermiso = false
    private lateinit var permissionResultCallBack: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        user = FirebaseAuth.getInstance().currentUser

        if(arguments != null){
            codigoLugar = requireArguments().getString("id_lugar","")
        }

        permissionResultCallBack = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            when (it) {
                true -> {
                    tienePermiso = true
                    println("permiso aceptado")
                }
                false -> {
                    print("Permiso denegado")
                }
            }
        }

        typefaceSolid = ResourcesCompat.getFont(requireContext(), R.font.font_awesome_solid_900)
        typefaceRegular = ResourcesCompat.getFont(requireContext(), R.font.font_awesome_regular_400)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoLugarBinding.inflate(inflater, container, false)

        Firebase.firestore
            .collection("lugares")
            .document(codigoLugar)
            .get()
            .addOnSuccessListener {
                var lugarF = it.toObject(Lugar::class.java)

                if (lugarF != null){
                    lugarF.key = it.id

                    cargarInformacion(lugarF)
                    dibujarEstrellas()
                }
            }
            .addOnFailureListener{
                Log.e("DETALLE_LUGAR", "${it.message}")
            }

        return binding.root
    }


    fun cargarInformacion(lugar:Lugar){

        binding.nombreLugar.text = lugar.nombre
        binding.descripcionLugar.text = lugar.descripcion
        binding.direccionLugar.text = lugar.direccion

        Firebase.firestore
            .collection("categorias")
            .whereEqualTo("id",lugar.idCategoria)
            .get()
            .addOnSuccessListener {
                for (dec in it){
                    binding.iconoCategoria.text = dec.toObject(Categoria::class.java).icono
                }

            }

        var telefonos = ""

        if(lugar.telefonos.isNotEmpty()) {
            for (tel in lugar.telefonos) {
                telefonos += "$tel, "
            }
            telefonos = telefonos.substring(0, telefonos.length - 2)
        }else{
            telefonos = "No hay teléfono"
        }

        binding.telefonoLugar.text = telefonos

        var horarios = ""

        for( horario in lugar.horarios ){
            for(dia in horario.diaSemana){
                horarios += "${dia.toString().lowercase().replaceFirstChar { it.uppercase() }}: ${horario.horaInicio}:00 - ${horario.horaCierre}:00\n"
            }
        }

        binding.horariosLugar.text = horarios

    }

    fun dibujarEstrellas(){

        var comentarios:ArrayList<Comentario> = ArrayList()

        Firebase.firestore
            .collection("lugares")
            .document(codigoLugar)
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
                if (lugarF != null){
                    lugarF.key = it.id
                    val calificacion = lugarF.obtenerCalificacionPromedio(comentarios)
                    for( i in 0 until calificacion){
                        (binding.estrellas.lista[i] as TextView).setTextColor( ContextCompat.getColor(requireContext(), R.color.yellow) )
                    }
                }
            }
    }

    companion object {
        fun newInstance(codigoLugar:String):InfoLugarFragment{
            val args = Bundle()
            args.putString("id_lugar", codigoLugar)
            val fragmento = InfoLugarFragment()
            fragmento.arguments = args
            return fragmento
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        gMap.uiSettings.isZoomControlsEnabled = true

        try {
            if (tienePermiso) {
                gMap.isMyLocationEnabled = true
                gMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                gMap.isMyLocationEnabled = false
                gMap.uiSettings.isMyLocationButtonEnabled = false
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }

        val estado = (requireActivity() as HomeActivity).estadoConexion

        if (estado) {
            val categorias = Categorias

            categorias.listar().forEach {
                Firebase.firestore.collection("categorias")
                    .add(it)
                    .addOnSuccessListener {  }
            }



            Firebase.firestore
                .collection("lugares")
                .document(codigoLugar)
                .get()
                .addOnSuccessListener {
                    var lugar = it.toObject(Lugar::class.java)
                    if (lugar != null) {
                        listaLugares.add(lugar)
                    }
                    gMap.addMarker(
                        MarkerOptions().position(LatLng(lugar!!.posicion.lat, lugar.posicion.lng))
                            .title(lugar!!.nombre).visible(true)
                    )!!.tag = lugar!!.key
                }


        }else{
            listaLugares.forEach {
                gMap.addMarker(
                    MarkerOptions().position(LatLng(it.posicion.lat, it.posicion.lng))
                        .title(it.nombre).visible(true)
                )!!.tag = it.key
            }

        }

        gMap.setOnInfoWindowClickListener { this }

        obtenerUbicacion()

        gMap.addMarker(MarkerOptions().position(defaultLocation).title("Marker en Armenia"))
        gMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation))
    }

    private fun obtenerUbicacion() {
        try {
            if (tienePermiso) {
                val ubicacionActual =
                    LocationServices.getFusedLocationProviderClient(requireContext()).lastLocation
                ubicacionActual.addOnCompleteListener(requireActivity()) {
                    if (it.isSuccessful) {
                        val ubicacion = it.result
                        if (ubicacion != null) {
                            val latlng = LatLng(ubicacion.latitude, ubicacion.longitude)
                            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15F))
                            gMap.addMarker(MarkerOptions().position(latlng).title("Marcador mapa"))
                        }
                    } else {
                        gMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                defaultLocation,
                                15F
                            )
                        )
                        gMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
}