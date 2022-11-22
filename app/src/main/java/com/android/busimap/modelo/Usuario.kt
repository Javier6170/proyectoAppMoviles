package com.android.busimap.modelo

import android.content.ContentValues

class Usuario() {


    var nombre: String = ""
    var nickname: String = ""

    var key: String = ""
    var uid: String = ""
    var imagenUser: String = ""
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

}