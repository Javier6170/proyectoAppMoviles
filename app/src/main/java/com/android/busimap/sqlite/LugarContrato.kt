package com.android.busimap.sqlite

import android.provider.BaseColumns

object LugarContrato:BaseColumns {
    val TABLE_NAME = "lugar"
    val ID = "id"
    val NOMBRE = "nombre"
    val DESCRIPCION = "descripcion"
    val LAT = "lat"
    val LNG = "lng"
    val DIRECCION = "direccion"
    val CATEGORIA = "idCategoria"
    val ID_CREADOR = "idCreador"
}