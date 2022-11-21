package com.android.busimap.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.busimap.R
import com.android.busimap.bd.Personas
import com.android.busimap.databinding.ActivityLoginBinding
import com.android.busimap.modelo.Rol
import com.android.busimap.modelo.Usuario
import com.android.busimap.sqlite.BusimapDbHelper
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    private lateinit var db: BusimapDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        db = BusimapDbHelper(this)

        var user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            hacerRedireccion(user)
        } else {

            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)

            binding.btnLogin.setOnClickListener { login() }
            binding.btnRegistro.setOnClickListener { irPantallaRegistro() }
            binding.btnOlvidarContrasena.setOnClickListener { irPantallaOlvidoContrasena() }
        }

    }

    fun irPantallaRegistro() {
        startActivity(Intent(this, RegistroActivity::class.java))
    }

    fun irPantallaOlvidoContrasena() {
        startActivity(Intent(this, OlvideContrasenaActivity::class.java))
    }

    fun login() {

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
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(correo.toString(), password.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        var user = FirebaseAuth.getInstance().currentUser

                        if (user != null) {
                            hacerRedireccion(user)
                        } else {
                            Snackbar.make(
                                binding.root,
                                "Este usuario no esta registrado!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Snackbar.make(
                            binding.root,
                            "No se ha podido iniciar sesion",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                .addOnFailureListener {

                }
        }
    }

    fun hacerRedireccion(user: FirebaseUser) {
        Firebase.firestore
            .collection("usuarios")
            .document(user.uid)
            .get()
            .addOnSuccessListener { u ->

                val rol = u.toObject(Usuario::class.java)?.rol
                var intent: Intent

                if (rol == Rol.CLIENTE) {
                    intent = Intent(this, HomeActivity::class.java)
                } else if (rol == Rol.MODERADOR) {
                    intent = Intent(this, HomeActivityModerador::class.java)
                } else {
                    intent = Intent(this, AdministradorActivity::class.java)
                }

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()

            }
    }

}