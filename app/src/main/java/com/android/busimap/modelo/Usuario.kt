package com.android.busimap.modelo

import android.content.ContentValues
import com.android.busimap.sqlite.UsuarioContrato

class Usuario() {


    var nombre: String = ""
    var nickname: String = ""

    var key: String = ""
    var uid: String = ""
    var rol: Rol? = Rol.CLIENTE


    constructor(
        nombre: String,
        nickname: String,
        rol: Rol
    ):this() {

        this.nombre = nombre
        this.nickname = nickname
        this.rol = rol
    }



    override fun toString(): String {
        return "Usuario(nickname='$nickname') ${super.toString()}"
    }

    fun toContentValues(): ContentValues {

        val values = ContentValues()
        values.put(UsuarioContrato.NOMBRE, nombre)
        values.put(UsuarioContrato.NICKNAME, nickname)
        //values.put(UsuarioContrato.CORREO, correo)
        //values.put(UsuarioContrato.PASSWORD, password)

        return values
    }

}