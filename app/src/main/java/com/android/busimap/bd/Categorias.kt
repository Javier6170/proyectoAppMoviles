package com.android.busimap.bd

import com.android.busimap.modelo.Categoria

object Categorias {

    private val categorias: ArrayList<Categoria> = ArrayList()

    init {

        categorias.add(Categoria(1, "Restaurante", "\uf2e7"))
        categorias.add(Categoria(2, "Hotel", "\uF594"))
        categorias.add(Categoria(3, "Almacen", "\uf54f"))
        categorias.add(Categoria(4, "Café", "\uF0F4"))
        categorias.add(Categoria(5, "Bar","\uf864"))
    }

    fun listar():ArrayList<Categoria>{
        return categorias
    }

    fun obtener(id:Int):Categoria?{
        return categorias.firstOrNull { c -> c.id == id }
    }
}