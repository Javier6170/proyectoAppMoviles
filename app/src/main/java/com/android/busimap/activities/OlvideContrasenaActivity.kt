package com.android.busimap.activities

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.busimap.bd.Usuarios
import com.android.busimap.databinding.ActivityOlvideContrasenaBinding
import com.android.busimap.modelo.Usuario
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class OlvideContrasenaActivity : AppCompatActivity() {

    private val SHORT_DURATION_MS = 4500
    lateinit var binding: ActivityOlvideContrasenaBinding
    lateinit var mAuth: FirebaseAuth
    lateinit var mDialog:ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOlvideContrasenaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        binding.btnRecuperarContrasena.setOnClickListener { recuperarContrasena() }

        mDialog = ProgressDialog(this)
    }

    fun recuperarContrasena() {
        val correo = binding.emailUsuario.text
        if (correo.isNotEmpty()){
            mDialog.setMessage("Esperando...")
            mDialog.setCanceledOnTouchOutside(false)
            mDialog.show()
            mAuth.sendPasswordResetEmail(correo.toString()).addOnCompleteListener {
                if (it.isSuccessful){
                    Snackbar.make(binding.root,  "Se ha enviado un correo para reestablecer tu contraseña", SHORT_DURATION_MS).show()
                }else{
                    Snackbar.make(binding.root,  "No se pudo enviar el correo de reestablecer contraseña", SHORT_DURATION_MS).show()
                }
                mDialog.dismiss()
                binding.emailUsuario.text.clear()
            }
        }

    }
}