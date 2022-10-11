package com.android.busimap.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.busimap.bd.Usuarios

import com.android.busimap.databinding.ActivityOlvideContrasenaBinding
import com.android.busimap.modelo.Usuario

class OlvideContrasenaActivity : AppCompatActivity() {

    lateinit var binding: ActivityOlvideContrasenaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOlvideContrasenaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRecuperarContrasena.setOnClickListener { recuperarContrasena() }

    }

    fun recuperarContrasena() {
        val correo = binding.emailUsuario.text
        val usuario:Usuario? = Usuarios.findUserByCorreo(correo)
        if (usuario!= null){
            Toast.makeText(this, "Contraseña: \n "+usuario.password, Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this, "El usuario no existe", Toast.LENGTH_LONG).show()
        }

    }
}