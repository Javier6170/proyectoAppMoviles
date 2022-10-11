package com.android.busimap.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.busimap.R
import com.android.busimap.adapter.LugarAdapter
import com.android.busimap.bd.Lugares
import com.android.busimap.bd.Moderadores
import com.android.busimap.bd.Usuarios
import com.android.busimap.databinding.ActivityHomeModeradorBinding
import com.android.busimap.fragmentos.*
import com.android.busimap.modelo.EstadoLugar
import com.android.busimap.modelo.Lugar
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class HomeActivityModerador : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{

    lateinit var binding: ActivityHomeModeradorBinding

    private lateinit var sh: SharedPreferences

    private var MENU_INICIO = "inicio"
    private var MENU_LUGARES_ACEPTADOS = "lugares_aceptados"
    private var MENU_LUGARES_DENEGADOS = "lugares_denegados"
    private var MENU_CONFIGURACIONES = "configuraciones"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeModeradorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding = ActivityHomeModeradorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)


        binding.btnMenu.setOnClickListener { abrirMenu() }

        sh = getSharedPreferences("sesion", Context.MODE_PRIVATE)

        val codigoUsuario = sh.getInt("codigo_usuario", 0)

        if( codigoUsuario != 0 ){
            val usuario = Moderadores.obtener(codigoUsuario)
            val encabezado = binding.navigationView.getHeaderView(0)

            encabezado.findViewById<TextView>(R.id.txt_nombreUser).text = usuario!!.nombre
            encabezado.findViewById<TextView>(R.id.txt_nickUser).text = usuario!!.correo
        }

        reemplazarFragmento(1, MENU_INICIO)
        binding.navigationView.setNavigationItemSelectedListener(this)


        binding.btnLogout.setOnClickListener { cerrarSesion() }


    }

    fun reemplazarFragmento(valor: Int, nombre: String) {

        var fragmento: Fragment = when (valor) {
            1 -> ListaLugaresModeradorFragment()
            2 -> AceptadosModeradorFragment()
            3 -> DenegadosModeradorFragment()
            4 -> ConfiguracionesFragment()
            else -> ConfiguracionesFragment()
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
                MENU_CONFIGURACIONES -> binding.navigationView.menu.getItem(2).isChecked = true
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

    fun abrirMenu() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navHomeModerador -> reemplazarFragmento(1, MENU_INICIO)
            R.id.navAceptados -> reemplazarFragmento(2, MENU_LUGARES_ACEPTADOS)
            R.id.navDenegados -> reemplazarFragmento(3, MENU_LUGARES_DENEGADOS)
            R.id.navConfiguracionModerador -> reemplazarFragmento(4,MENU_CONFIGURACIONES )
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


}
