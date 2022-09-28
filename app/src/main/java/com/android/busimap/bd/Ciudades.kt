package com.android.busimap.bd

import com.android.busimap.modelo.Ciudad

object Ciudades {
    private val lista: ArrayList<Ciudad> = ArrayList()

    init {
        lista.add(Ciudad(1, "Armenia"))
        lista.add(Ciudad(2, "Periera"))
        lista.add(Ciudad(3, "Bogota"))
        lista.add(Ciudad(4, "Calarca"))
        lista.add(Ciudad(5, "Manizales"))
    }

    fun listar(): ArrayList<Ciudad>{
        return lista;
    }

    fun obtener(id: Int): Ciudad?{
        return lista.firstOrNull{c -> c.id == id}
    }
}