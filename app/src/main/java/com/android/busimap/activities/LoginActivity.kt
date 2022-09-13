package com.android.busimap.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.busimap.R
import com.android.busimap.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener{login()}
    }

    fun login(){
        val correo = binding.emailUsuario.text
        val password = binding.passwordUsuario.text

        if (correo.isNotEmpty() && password.isNotEmpty()){
            Toast.makeText(this, "Sus datos son correctos", Toast.LENGTH_LONG).show()
            println("hola")
        }else{
            Toast.makeText(this, "Los campos son obligatorios", Toast.LENGTH_LONG).show()
        }
    }
}