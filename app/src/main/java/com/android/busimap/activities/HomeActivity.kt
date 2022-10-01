package com.android.busimap.activities

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.android.busimap.R
import com.android.busimap.bd.Categorias
import com.android.busimap.databinding.ActivityHomeBinding
import com.android.busimap.fragmentos.*
import com.android.busimap.modelo.Lugar
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var MENU_INICIO = "inicio"
    private var MENU_MIS_LUGARES = "mis_lugares"
    private var MENU_FAVORITOS = "favoritos"
    private var MENU_NOTIFICACIONES = "notifiaciones"
    private var MENU_CONFIGURACIONES = "configuraciones"
    private var MENU_AYUDA = "ayuda"
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        lateinit var listaCategorias:ArrayList<Categorias>

        super.onCreate(savedInstanceState)


        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

        reemplazarFragmento(1, MENU_INICIO)
        binding.btnMenu.setOnClickListener { abrirMenu() }
        binding.btnCrearNegocio.setOnClickListener { abrirCrearNegocio() }

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

        if(count > 0) {
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

    fun cerrarSesion(){
        val sh = getSharedPreferences("sesion", Context.MODE_PRIVATE).edit()
        sh.clear()
        sh.commit()
        finish()
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
            R.id.navAyuda -> reemplazarFragmento(5, MENU_AYUDA)
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


}