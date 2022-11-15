package com.android.busimap.modelo

class Moderador: Persona {

    var key: String = ""

    constructor(id: Int, nombre: String, correo: String, password: String):super(id, nombre, correo, password){
    }
    override fun toString(): String {
        return "Moderador() ${super.toString()}"
    }
}