package com.android.busimap.bd

import com.android.busimap.modelo.Comentario
import com.android.busimap.modelo.Usuario

object Comentarios {

    private val lista:ArrayList<Comentario> = ArrayList()

    init {

    }
/*
    fun listar(idLugar: String):ArrayList<Comentario>{
        return lista.filter { c -> c.idLugar == idLugar }.toCollection(ArrayList())
    }


 */
    fun crear(comentario: Comentario):Comentario{
        comentario.id = lista.size+1
        lista.add( comentario )
        return comentario
    }
}