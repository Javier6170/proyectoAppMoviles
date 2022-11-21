package com.android.busimap.modelo

import java.time.LocalDate
import java.util.*

class Comentario() {

    constructor(texto: String, idUsuario: String,calificacion: Int):this() {
        this.texto = texto
        this.idUsuario = idUsuario
        this.calificacion = calificacion
    }

    constructor(id: Int, texto: String, idUsuario: String, calificacion: Int) : this() {
        this.id = id
        this.texto = texto
        this.idUsuario = idUsuario
        this.calificacion = calificacion
    }

    var texto: String = ""
    var key: String = ""
    var idUsuario: String = ""
    var calificacion: Int = 0
    var id: Int = 0
    var fecha: Date = Date()


}