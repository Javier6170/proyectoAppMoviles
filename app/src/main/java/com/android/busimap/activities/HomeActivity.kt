package com.android.busimap.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.android.busimap.R
import com.android.busimap.bd.Categorias
import com.android.busimap.bd.Usuarios
import com.android.busimap.databinding.ActivityHomeBinding


import com.android.busimap.fragmentos.*
import com.android.busimap.modelo.Lugar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    OnMapReadyCallback {

    private var MENU_INICIO = "inicio"
    private var MENU_MIS_LUGARES = "mis_lugares"
    private var MENU_FAVORITOS = "favoritos"
    private var MENU_NOTIFICACIONES = "notifiaciones"
    private var MENU_CONFIGURACIONES = "configuraciones"
    private var MAPA = "mapa"
    private var MENU_AYUDA = "ayuda"
    private lateinit var binding: ActivityHomeBinding
    private lateinit var mMap: GoogleMap
    private lateinit var sh:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

        binding.btnMenu.setOnClickListener { abrirMenu() }
        binding.btnCrearNegocio.setOnClickListener { abrirCrearNegocio() }
        binding.btnLogout.setOnClickListener { cerrarSesion() }



        sh = getSharedPreferences("sesion", Context.MODE_PRIVATE)

        val codigoUsuario = sh.getInt("codigo_usuario", 0)

        if( codigoUsuario != 0 ){
            val usuario = Usuarios.obtener(codigoUsuario)
            val encabezado = binding.navigationView.getHeaderView(0)

            encabezado.findViewById<TextView>(R.id.txt_nombreUser).text = usuario!!.nombre
            encabezado.findViewById<TextView>(R.id.txt_nickUser).text = usuario!!.correo
        }

        reemplazarFragmento(1, MENU_INICIO)
        binding.navigationView.setNavigationItemSelectedListener(this)


        supportFragmentManager.beginTransaction()
            .replace(binding.mapaCreado.id, MapaFragment())
            .addToBackStack(MAPA)
            .commit()
    }


    fun reemplazarFragmento(valor: Int, nombre: String) {

        var fragmento: Fragment = when (valor) {
            1 -> InicioFragment()
            2 -> MisNegociosFragment()
            3 -> FavoritosFragment()
            4 -> NotificacionsFragment()
            5 -> ConfiguracionesFragment()
            6 -> AyudaFragment()
            else -> AyudaFragment()
        }


        supportFragmentManager.beginTransaction()
            .replace(binding.contenidoPrincipal.id, fragmento)
            .addToBackStack(nombre)
            .commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val count = supportFragmentManager.backStackEntryCount

        if (count > 0) {
            val nombre = supportFragmentManager.getBackStackEntryAt(count - 1).name
            when (nombre) {
                MENU_INICIO -> binding.navigationView.menu.getItem(0).isChecked = true
                MENU_MIS_LUGARES -> binding.navigationView.menu.getItem(1).isChecked = true
                MENU_FAVORITOS -> binding.navigationView.menu.getItem(2).isChecked = true
                MENU_NOTIFICACIONES -> binding.navigationView.menu.getItem(3).isChecked = true
                MENU_CONFIGURACIONES -> binding.navigationView.menu.getItem(4).isChecked = true
                MENU_AYUDA -> binding.navigationView.menu.getItem(5).isChecked = true
                else -> binding.navigationView.menu.getItem(6).isChecked = true
            }
        }

    }

    fun cerrarSesion() {
        val sh = getSharedPreferences("sesion", Context.MODE_PRIVATE).edit()
        sh.clear()
        sh.commit()
        finish()
        startActivity(Intent(this, LoginActivity::class.java))
    }


    fun abrirCrearNegocio() {
        val intent = Intent(this, VistaCrearNegocio::class.java)
        startActivity(intent)
    }


    fun abrirMenu() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navHome -> reemplazarFragmento(1, MENU_INICIO)
            R.id.navMisNegocios -> reemplazarFragmento(2, MENU_MIS_LUGARES)
            R.id.navFavoritos -> reemplazarFragmento(3, MENU_FAVORITOS)
            R.id.navNotificaciones -> reemplazarFragmento(4, MENU_NOTIFICACIONES)
            R.id.navConfiguracion -> reemplazarFragmento(5, MENU_CONFIGURACIONES)
            R.id.navAyuda -> reemplazarFragmento(6, MENU_AYUDA)
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}