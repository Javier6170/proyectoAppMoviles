package com.android.busimap.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.busimap.R
import com.android.busimap.bd.Personas
import com.android.busimap.databinding.ActivityLoginBinding
import com.android.busimap.modelo.Moderador
import com.android.busimap.modelo.Usuario

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sp = getSharedPreferences("sesion", Context.MODE_PRIVATE)

        val correo = sp.getString("correo_usuario", "")
        val tipo = sp.getString("tipo_usuario", "")

        if(correo!!.isNotEmpty() && tipo!!.isNotEmpty()){

            when(tipo){
                "usuario" -> startActivity(Intent(this, MainActivity::class.java))
                "moderador" -> startActivity( Intent(this, HomeActivityModerador::class.java) )
                "admin" -> startActivity( Intent(this, AdministradorActivity::class.java) )
            }

            finish()

        }else{

            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)

            binding.btnLogin.setOnClickListener { login() }
            binding.btnRegistro.setOnClickListener { irPantallaRegistro() }
        }

    }

    fun irPantallaRegistro() {
        Log.v(LoginActivity::class.java.simpleName, "Pasar pantalla registro")
        val intent = Intent(this, RegistroActivity::class.java)
        startActivity(intent)
    }

    private fun login() {

        val correo = binding.emailUsuario.text
        var password = binding.passwordUsuario.text

        if (correo.isEmpty()) {
            binding.emailLayout.isErrorEnabled = true
            binding.emailLayout.error = getString(R.string.es_obligatorio)
        } else {
            binding.emailLayout.error = null
        }

        if (password.isEmpty()) {
            binding.passwordLayout.error = getString(R.string.es_obligatorio)
        } else {
            binding.passwordLayout.error = null
        }

        if (correo.isNotEmpty() && password.isNotEmpty()) {

            try {
                val persona = Personas.login(correo.toString(), password.toString())

                if (persona != null) {

                    when (persona) {
                        is Usuario -> startActivity(Intent(this, HomeActivity::class.java))
                        is Moderador -> startActivity(
                            Intent(
                                this,
                                HomeActivityModerador::class.java
                            )
                        )
                    }
                } else {
                    Toast.makeText(this, getString(R.string.datos_incorrectos), Toast.LENGTH_LONG)
                        .show()
                }

            } catch (e: Exception) {
                Toast.makeText(this, getString(R.string.datos_incorrectos), Toast.LENGTH_LONG)
                    .show()
            }

        }

    }


}