package com.android.busimap.bd

import com.android.busimap.modelo.Moderador
import com.android.busimap.modelo.Usuario

object Moderadores {
    private val lista:ArrayList<Moderador> = ArrayList()

    init {
        lista.add( Moderador(1, "Moderador1", "mode1@email.com", "1234"))
        lista.add( Moderador(2, "Moderador2", "mode2@email.com", "1234"))
    }

    fun listar():ArrayList<Moderador>{
        return lista
    }

    fun agregar(moderador: Moderador){
        lista.add(moderador)
    }

    fun eliminar(moderador: Moderador){
        lista.remove(moderador)
    }

    fun obtener(id:Int): Moderador?{
        return lista.firstOrNull { a -> a.id == id }
    }
}