package com.android.busimap.activities

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.android.busimap.R
import com.android.busimap.bd.Usuarios
import com.android.busimap.databinding.ActivityHomeBinding


import com.android.busimap.fragmentos.*
import com.android.busimap.modelo.Usuario
import com.android.busimap.sqlite.BusimapDbHelper
import com.android.busimap.util.EstadoConexion
import com.android.busimap.util.Idioma
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    OnMapReadyCallback {

    private var MENU_INICIO = "inicio"
    private var MENU_MIS_LUGARES = "mis_lugares"
    private var MENU_FAVORITOS = "favoritos"
    private var MENU_CONFIGURACIONES = "configuraciones"
    private var MAPA = "mapa"
    private lateinit var binding: ActivityHomeBinding
    private lateinit var mMap: GoogleMap

    var estadoConexion: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        comprobarConexionInternet()

        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)


        binding.btnMenu.setOnClickListener { abrirMenu() }
        binding.btnCrearNegocio.setOnClickListener { abrirCrearNegocio() }
        binding.btnLogout.setOnClickListener { cerrarSesion() }


        val encabezado = binding.navigationView.getHeaderView(0)

        var user = FirebaseAuth.getInstance().currentUser



        if (user != null) {

            Firebase.firestore.collection("usuarios")
                .document(user!!.uid)
                .get()
                .addOnSuccessListener {
                    val userF = it.toObject(Usuario::class.java)

                    var imagen = userF!!.imagenUser

                    if (imagen!="" || imagen == null){
                        Glide.with(binding.root.context)
                            .load(imagen)
                            .into(encabezado.findViewById(R.id.imagenUser))

                    }
                }

            encabezado.findViewById<TextView>(R.id.txt_nombreUser).text = user.email
            Firebase.firestore
                .collection("usuarios")
                .document(user.uid)
                .get()
                .addOnSuccessListener { u ->
                    val nickname = u.toObject(Usuario::class.java)?.nickname
                    encabezado.findViewById<TextView>(R.id.txt_nickUser).text = nickname
                }
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
            4 -> ConfiguracionesFragment()
            else -> InicioFragment()
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
                MENU_INICIO -> binding.navigationView.menu.getItem(1).isChecked = true
                MENU_MIS_LUGARES -> binding.navigationView.menu.getItem(2).isChecked = true
                MENU_FAVORITOS -> binding.navigationView.menu.getItem(3).isChecked = true
                MENU_CONFIGURACIONES -> binding.navigationView.menu.getItem(4).isChecked = true
                else -> binding.navigationView.menu.getItem(1).isChecked = true
            }
        }

    }

    fun cerrarSesion() {
        Firebase.auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        this.startActivity(intent)
        this.finish()

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
            R.id.navConfiguracion -> reemplazarFragmento(4, MENU_CONFIGURACIONES)
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

    fun cambiarIdioma() {
        val intent = intent
        if (intent != null) {
            intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            finish()
            startActivity(intent)
        }

    }

    override fun attachBaseContext(newBase: Context?) {
        val localeUpdatedContext: ContextWrapper? = Idioma.cambiarIdioma(newBase!!)
        super.attachBaseContext(localeUpdatedContext)
    }

    fun comprobarConexionInternet() {
        val conectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        conectivityManager.registerDefaultNetworkCallback(EstadoConexion(::comprobarConexion))
    }

    fun comprobarConexion(estado: Boolean) {
        estadoConexion = estado
        Log.e("ESTADO CONEXION", estadoConexion.toString())
    }


}