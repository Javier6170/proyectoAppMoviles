package com.android.busimap.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.android.busimap.R
import com.android.busimap.bd.Administradores
import com.android.busimap.bd.Usuarios
import com.android.busimap.databinding.ActivityAdministradorBinding
import com.android.busimap.databinding.ActivityHomeModeradorBinding
import com.android.busimap.fragmentos.*
import com.google.android.material.navigation.NavigationView

class AdministradorActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var sh: SharedPreferences
    lateinit var binding: ActivityAdministradorBinding
    private var MENU_INICIO = "inicio"
    private var MENU_CREAR_MODE = "crear_moderador"
    private var MENU_ELIMINAR_MODE = "crear_eliminar_mode"
    private var MENU_CONFIGURACIONES = "configuraciones"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdministradorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

        sh = getSharedPreferences("sesion", Context.MODE_PRIVATE)

        val codigoUsuario = sh.getInt("codigo_usuario", 0)

        if( codigoUsuario != 0 ){
            val usuario = Administradores.obtener(codigoUsuario)
            val encabezado = binding.navigationView.getHeaderView(0)

            encabezado.findViewById<TextView>(R.id.txt_nombreUser).text = usuario!!.nombre
            encabezado.findViewById<TextView>(R.id.txt_nickUser).text = usuario!!.correo
        }
        reemplazarFragmento(1, MENU_INICIO)
        binding.navigationView.setNavigationItemSelectedListener(this)

        binding.btnMenu.setOnClickListener { abrirMenu() }
        binding.btnLogout.setOnClickListener { cerrarSesion() }

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

    fun reemplazarFragmento(valor: Int, nombre: String) {

        var fragmento: Fragment = when (valor) {
            1 -> InicioAdministradorFragment()
            2 -> CrearModeradorAdminFragment()
            3 -> EliminarModeradorFragment()
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navHomeAdministrador -> reemplazarFragmento(1, MENU_INICIO)
            R.id.navCrearModerador -> reemplazarFragmento(2, MENU_CREAR_MODE)
            R.id.navEliminarModerador -> reemplazarFragmento(3, MENU_ELIMINAR_MODE)
            R.id.navConfiguracionModerador -> reemplazarFragmento(4,MENU_CONFIGURACIONES )
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}