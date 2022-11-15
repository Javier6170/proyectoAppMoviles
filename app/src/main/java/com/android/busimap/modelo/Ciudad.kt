package com.android.busimap.modelo

class Ciudad() {


    constructor(id: Int, nombre: String) : this() {
        this.id = id
        this.nombre = nombre
    }

    var id: Int = 0
    var nombre: String = ""
    var key: String = ""

    override fun toString(): String {
        return "Ciudad(id=$id, nombre='$nombre')"
    }


}