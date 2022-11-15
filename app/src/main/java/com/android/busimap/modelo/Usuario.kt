package com.android.busimap.modelo

import android.content.ContentValues
import com.android.busimap.sqlite.UsuarioContrato

class Usuario :
    Persona {

    var nickname: String = ""
    var favoritos: ArrayList<Int> = ArrayList()
    var key: String = ""

    constructor(
        id: Int,
        nombre: String,
        nickname: String,
        correo: String,
        password: String
    ) : super(id, nombre, correo, password) {
        this.nickname = nickname
    }

    var lugaresFavoritos: ArrayList<Lugar> = ArrayList()

    fun esFavorito(codigo:Int):Boolean{
        return favoritos.contains(codigo)
    }

    override fun toString(): String {
        return "Usuario(nickname='$nickname') ${super.toString()}"
    }

    fun toContentValues(): ContentValues {

        val values = ContentValues()
        values.put(UsuarioContrato.NOMBRE, nombre)
        values.put(UsuarioContrato.NICKNAME, nickname)
        values.put(UsuarioContrato.CORREO, correo)
        values.put(UsuarioContrato.PASSWORD, password)

        return values
    }

}