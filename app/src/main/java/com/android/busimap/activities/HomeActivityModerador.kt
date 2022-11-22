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
import com.android.busimap.databinding.ActivityHomeModeradorBinding
import com.android.busimap.fragmentos.*
import com.android.busimap.modelo.Usuario
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeActivityModerador : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{

    lateinit var binding: ActivityHomeModeradorBinding


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



        var user = FirebaseAuth.getInstance().currentUser
        val encabezado = binding.navigationView.getHeaderView(0)

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
        Firebase.auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        this.startActivity(intent)
        this.finish()
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
