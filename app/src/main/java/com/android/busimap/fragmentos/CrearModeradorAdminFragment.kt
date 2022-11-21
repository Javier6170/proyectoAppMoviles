package com.android.busimap.fragmentos

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.busimap.R
import com.android.busimap.activities.LoginActivity
import com.android.busimap.databinding.FragmentCrearModeradorAdminBinding
import com.android.busimap.modelo.Rol
import com.android.busimap.modelo.Usuario
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class CrearModeradorAdminFragment : Fragment() {


    lateinit var binding: FragmentCrearModeradorAdminBinding
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val builder: AlertDialog.Builder? = activity?.let { AlertDialog.Builder(it) }
        builder?.setView(R.layout.dialogo_progreso)
        dialog = builder?.create()!!


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCrearModeradorAdminBinding.inflate(inflater, container, false)
        binding.btnRegistro.setOnClickListener { registraModerador() }
        return binding.root
    }

    companion object {

    }

    fun registraModerador() {

        val nombre = binding.nombreUsuario.text
        val correo = binding.emailUsuario.text
        val password = binding.passwordUsuario.text
        val repetirPassword = binding.repetirPasswordUser.text

        if (nombre.isEmpty()) {
            binding.nombreLayout.error = "Debes llenar este campo!"
        } else {
            binding.nombreLayout.error = null
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
            if (correo.isNotEmpty() && password.isNotEmpty() && nombre.isNotEmpty()  && repetirPassword.isNotEmpty()) {

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
                                    nombre.toString(),
                                    Rol.CLIENTE
                                )

                                Firebase.firestore
                                    .collection("usuarios")
                                    .document(usuario.uid)
                                    .set(usuarioRegistro)
                                    .addOnSuccessListener {
                                        setDialog(false)
                                        Toast.makeText(
                                            activity,
                                            "Se ha registrador correctamente",
                                            Toast.LENGTH_LONG
                                        ).show()
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
        }

    }

    private fun verificarEmail(user: FirebaseUser) {
        activity?.let {
            user.sendEmailVerification().addOnCompleteListener(it) {
                if (it.isSuccessful) {
                    Toast.makeText(context, "Email enviado", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setDialog(show: Boolean) {
        if (show) dialog.show() else dialog.dismiss()
    }

}