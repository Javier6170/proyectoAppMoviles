package com.android.busimap.modelo

class Administrador: Persona {


    var key: String = ""

    constructor(id: Int, nombre: String, correo: String, password: String):super(id, nombre, correo, password){
    }

    override fun toString(): String {
        return "Administrador() ${super.toString()}"
    }

}