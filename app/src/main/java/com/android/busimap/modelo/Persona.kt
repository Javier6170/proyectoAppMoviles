package com.android.busimap.modelo

open class Persona() {
    constructor( id:Int,  nombre:String,  correo:String,  password:String):this(){
        this.id = id
        this.nombre = nombre
        this.correo = correo
        this.password = password
    }
    var id:Int=0
    var nombre:String=""
    var correo:String=""
    var password:String = ""

    override fun toString(): String {
        return "Persona(id=$id, nombre='$nombre', correo='$correo', password='$password')"
    }
}