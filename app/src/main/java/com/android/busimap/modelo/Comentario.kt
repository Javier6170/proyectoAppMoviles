package com.android.busimap.modelo

import java.time.LocalDate
import java.util.*

class Comentario() {

    constructor(texto: String, idUsuario: Int, idLugar: Int, calificacion: Int):this() {
        this.texto = texto
        this.idUsuario = idUsuario
        this.idLugar = idLugar
        this.calificacion = calificacion
    }

    constructor(id: Int, texto: String, idUsuario: Int, idLugar: Int, calificacion: Int) : this() {
        this.id = id
        this.texto = texto
        this.idUsuario = idUsuario
        this.idLugar = idLugar
        this.calificacion = calificacion
    }

    var texto: String = ""
    var key: String = ""
    var idUsuario: Int = 0
    var idLugar: Int = 0
    var calificacion: Int = 0
    var id: Int = 0
    var fecha: Date = Date()

    override fun toString(): String {
        return "Comentario(id=$id, texto='$texto', idUsuario=$idUsuario, idLugar=$idLugar, calificacion=$calificacion, fecha=$fecha)"
    }

}