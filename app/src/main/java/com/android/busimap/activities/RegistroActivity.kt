package com.android.busimap.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.busimap.bd.Usuarios
import com.android.busimap.databinding.ActivityRegistroBinding
import com.android.busimap.modelo.Rol
import com.android.busimap.modelo.Usuario
import com.android.busimap.sqlite.BusimapDbHelper
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.android.busimap.R

class RegistroActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegistroBinding
    private lateinit var db: BusimapDbHelper
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(R.layout.dialogo_progreso)
        dialog = builder.create()


        db = BusimapDbHelper(this)

        binding.btnRegistro.setOnClickListener { registrarse() }

    }

    fun registrarse() {

        setDialog(true)

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

                FirebaseAuth
                    .getInstance()
                    .createUserWithEmailAndPassword(correo.toString(), password.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val usuario = FirebaseAuth.getInstance().currentUser


                            if (usuario != null) {
                                verificarEmail(usuario)

                                val usuarioRegistro = Usuario(
                                    nombre.toString(),
                                    nickname.toString(),
                                    Rol.CLIENTE
                                )

                                Firebase.firestore
                                    .collection("usuarios")
                                    .document(usuario.uid)
                                    .set(usuarioRegistro)
                                    .addOnSuccessListener {
                                        setDialog(false)
                                        Toast.makeText(
                                            this,
                                            "Se ha registrador correctamente",
                                            Toast.LENGTH_LONG
                                        ).show()


                                        val intent = Intent(this, LoginActivity::class.java)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        startActivity(intent)
                                        finish()
                                    }
                            }
                        }
                    }.addOnFailureListener {
                        setDialog(false)
                        Snackbar.make(binding.root, it.message.toString(), Toast.LENGTH_LONG).show()
                    }
            } else {
                setDialog(false)
            }
        } else {
            Toast.makeText(this, "Debes colocar contraseñas iguales!!", Toast.LENGTH_LONG).show()
            setDialog(false)
        }

    }

    private fun verificarEmail(user: FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(baseContext, "Email enviado", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(baseContext, "Error", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun setDialog(show: Boolean) {
        if (show) dialog.show() else dialog.dismiss()
    }

}