package com.android.busimap.fragmentos

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.android.busimap.R
import com.android.busimap.activities.DetalleLugarActivity
import com.android.busimap.activities.HomeActivity
import com.android.busimap.bd.Lugares
import com.android.busimap.databinding.FragmentMapaBinding
import com.android.busimap.databinding.FragmentMisNegociosBinding
import com.android.busimap.modelo.EstadoLugar
import com.android.busimap.sqlite.BusimapDbHelper
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapaFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    lateinit var bd: BusimapDbHelper
    lateinit var binding: FragmentMapaBinding
    lateinit var gMap: GoogleMap
    private var tienePermiso = false
    private val defaultLocation = LatLng(4.550923, -75.6557201)
    private lateinit var permissionResultCallBack: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getLocationPermission()

        bd = BusimapDbHelper(requireContext())

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

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapaBinding.inflate(inflater, container, false)

        val mapFrgament = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFrgament.getMapAsync(this)

        return binding.root
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
            Lugares.listarPorEstado(EstadoLugar.ACEPTADO).forEach {
                gMap.addMarker(
                    MarkerOptions().position(LatLng(it.posicion.lat, it.posicion.lng))
                        .title(it.nombre).visible(true)
                )!!.tag = it.id
                bd.crearLugar(it)
            }

        }else{
            bd.listarLugares().forEach {
                gMap.addMarker(
                    MarkerOptions().position(LatLng(it.posicion.lat, it.posicion.lng))
                        .title(it.nombre).visible(true)
                )!!.tag = it.id
            }
        }

        gMap.setOnInfoWindowClickListener { this }

        obtenerUbicacion()

        gMap.addMarker(MarkerOptions().position(defaultLocation).title("Marker en Armenia"))
        gMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation))

    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            tienePermiso = true
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
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

    override fun onInfoWindowClick(p0: Marker) {
       val intent = Intent(requireActivity(), DetalleLugarActivity::class.java)
        intent.putExtra("codigo", p0.tag.toString().toInt())
        requireContext().startActivity(intent)
    }


}