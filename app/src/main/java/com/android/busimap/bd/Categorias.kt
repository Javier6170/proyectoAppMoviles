package com.android.busimap.bd

import com.android.busimap.modelo.Categoria
import com.android.busimap.modelo.Comentario

object Categorias {

    private val lista:ArrayList<Categoria> = ArrayList()

    init {
        lista.add( Categoria(1, "Hotel", "\uf594") )
        lista.add( Categoria(1, "Restaurante", "\uf2e7") )
        lista.add( Categoria(1, "Almacen", "\uf54f") )
        lista.add( Categoria(1, "Cafe", "\uf0f4") )
    }

    fun listar():ArrayList<Categoria>{
        return lista
    }
}