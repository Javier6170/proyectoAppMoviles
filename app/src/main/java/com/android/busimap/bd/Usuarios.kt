package com.android.busimap.bd

import com.android.busimap.modelo.Usuario

object Usuarios {

    private val usuarios: ArrayList<Usuario> = ArrayList()

    init {
        usuarios.add(
            Usuario(
                1,
                "Javier Rodriguez Marulanda",
                "Javiprl",
                "rodriguez.javier.6170@eam.edu.co",
                "12345"
            )
        )
        usuarios.add(Usuario(1, "Santiago Beltran Florez", "santi", "santi@eam.edu.co", "12345678"))
        usuarios.add(Usuario(1, "carlos", "carlos", "carlos@email.com", "123"))
    }

    fun listar():ArrayList<Usuario>{
        return usuarios
    }

    fun agregar(usuario: Usuario){
        usuarios.add(usuario)
    }

    fun obtener(id:Int): Usuario?{
        return usuarios.firstOrNull { u -> u.id == id }
    }

    fun login(correo: String, password: String): Usuario{
        val user = usuarios.first { it -> it.contrasena == password && it.correo == correo }
        return user
    }

    /*

    fun findUserByCorreo(correo: String): Usuario? {
        usuarios.forEach {
            if (correo.equals(it.correo)) {
                return it
            }
        }
        return null
    }*/

}