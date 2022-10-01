package com.android.busimap.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.busimap.R
import com.android.busimap.bd.Usuarios
import com.android.busimap.databinding.ActivityRegistroBinding
import com.android.busimap.modelo.Usuario

class RegistroActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegistro.setOnClickListener { registrarse() }

    }

    fun registrarse() {

        val nombre = binding.nombreUsuario.text
        val nickname = binding.nicknameUsuario.text
        val correo = binding.emailUsuario.text
        val password = binding.passwordUsuario.text
        val repetirPassword = binding.repetirPasswordUser.text

        if (nombre.isEmpty()) {
            binding.nombreLayout.error = "Debes llenar este campo!"
        } else {
            binding.nombreLayout.error = null
        }

        if (nickname.isEmpty()) {
            binding.nicknameLayout.error = "Debes llenar este campo!"
        } else {
            binding.nicknameLayout.error = null
        }

        if (correo.isEmpty()) {
            binding.emailLayout.error = "Debes llenar este campo!"
        } else {
            binding.emailLayout.error = null
        }

        if (password.isEmpty()) {
            binding.passwordLayout.error = "Debes llenar este campo!"
        } else {
            binding.passwordLayout.error = null
        }

        if (repetirPassword.isEmpty()) {
            binding.repetirPasswordLayout.error = "Debes llenar este campo!"
        } else {
            binding.repetirPasswordLayout.error = null
        }

        if (repetirPassword !== password) {
            if (correo.isNotEmpty() && password.isNotEmpty() && nombre.isNotEmpty() && nickname.isNotEmpty() && repetirPassword.isNotEmpty()) {
                val user = Usuario(1, nombre.toString(), nickname.toString(), correo.toString(), password.toString())
                Usuarios.agregar(user)
                Toast.makeText(this, "Se ha registrador correctamente", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Debes colocar contraseñas iguales!!", Toast.LENGTH_LONG).show()
        }

    }
}