package com.android.busimap.bd

import com.android.busimap.modelo.Horario
import com.android.busimap.modelo.Lugar
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

object Lugares {
    private val lista: ArrayList<Lugar> = ArrayList()

    init {
        val horario1 = Horario(1, Horarios.obtenerTodos(), 12, 20)
        val horario2 = Horario(2, Horarios.obtenerFinSemana(), 9, 20)
        val horario3 = Horario(3, Horarios.obtenerEntreSemana(), 14, 23)


        val lugar1 =
            Lugar(1, "Cafe ABC", "Excelente café para compartir", 2, true, 4, 73.3434f, 45.545f, 1)
        lugar1.horarios.add(horario2)



        lista.add(lugar1)
    }

    fun listaAprobados(): ArrayList<Lugar> {
        return lista.filter { l -> l.estado }.toCollection(ArrayList())
    }

    fun listaRechazados(): ArrayList<Lugar> {
        return lista.filter { l -> !l.estado }.toCollection(ArrayList())
    }

    fun obtener(id: Int): Lugar? {
        return lista.firstOrNull { l -> l.id == id }
    }

    fun buscarNombre(nombre: String): ArrayList<Lugar> {
        return lista.filter { l -> l.nombre == nombre && l.estado }.toCollection(ArrayList())
    }

    fun crear(lugar: Lugar) {
        lista.add(lugar)
    }

    fun buscarCiudad(codigoCiudad: Int): ArrayList<Lugar> {
        return lista.filter { l -> l.idCiudad == codigoCiudad && l.estado }
            .toCollection(ArrayList())
    }

    fun buscarCategoria(codigoCategoria: Int): ArrayList<Lugar> {
        return lista.filter { l -> l.idCategoria == codigoCategoria && l.estado }
            .toCollection(ArrayList())
    }
}