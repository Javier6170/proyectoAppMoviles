package com.android.busimap.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.busimap.bd.Usuarios
import com.android.busimap.databinding.ActivityOlvideContrasenaBinding
import com.android.busimap.modelo.Usuario
import com.google.android.material.snackbar.Snackbar

class OlvideContrasenaActivity : AppCompatActivity() {

    private val SHORT_DURATION_MS = 4500
    lateinit var binding: ActivityOlvideContrasenaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOlvideContrasenaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRecuperarContrasena.setOnClickListener { recuperarContrasena() }

    }

    fun recuperarContrasena() {
        val correo = binding.emailUsuario.text
        val usuario:Usuario? = Usuarios.findUserByCorreo(correo.toString())
        if (usuario!= null){
            Snackbar.make(binding.root,  "Contraseña: \n "+usuario.password, SHORT_DURATION_MS).show()
        }else{
            Snackbar.make(binding.root,  "El usuario no existe", SHORT_DURATION_MS).show()
        }

    }
}