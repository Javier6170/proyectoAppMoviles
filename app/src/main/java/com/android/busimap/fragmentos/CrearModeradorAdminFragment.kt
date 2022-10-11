package com.android.busimap.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.busimap.R
import com.android.busimap.bd.Moderadores
import com.android.busimap.databinding.FragmentCrearModeradorAdminBinding
import com.android.busimap.modelo.Moderador


class CrearModeradorAdminFragment : Fragment() {


    lateinit var binding: FragmentCrearModeradorAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                val user = Moderador(Moderadores.listar().size+1, nombre.toString(), correo.toString(), password.toString())
                Moderadores.agregar(user)
                Toast.makeText(activity, "Se ha registrador correctamente", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(activity, "Debes colocar contraseñas iguales!!", Toast.LENGTH_LONG).show()
            }
        }

    }
}