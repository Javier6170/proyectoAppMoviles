package com.android.busimap.modelo

class Usuario(id: Int, nombre: String, var nickname:String, correo: String, password: String): Persona(id, nombre, correo, password) {
    override fun toString(): String {
        return "Usuario(nickname='$nickname') ${super.toString()}"
    }
}