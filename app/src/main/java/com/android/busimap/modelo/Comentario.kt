package com.android.busimap.modelo

import java.time.LocalDate
import java.util.*

class Comentario(var id:Int,
                 var texto:String,
                 var idUsuario:Int,
                 var idLugar:Int,
                 var calificacion:Int) {
    var fecha:Date = Date()

    override fun toString(): String {
        return "Comentario(id=$id, texto='$texto', idUsuario=$idUsuario, idLugar=$idLugar, calificacion=$calificacion, fecha=$fecha)"
    }
}