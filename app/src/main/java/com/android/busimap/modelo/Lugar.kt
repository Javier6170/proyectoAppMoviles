package com.android.busimap.modelo

import java.time.LocalDate

class Lugar(var id: Int, var nombre: String,
            var descripcion: String,
            var imagenes:List<String>,
            var idCreador: Int,
            var estado: Boolean,
            var idCategoria: Int,
            var latitud: Float,
            var longitud: Float,
            var idCiudad: Int,
            var fecha: LocalDate
) {

    var telefonos: List<String> = ArrayList()
}