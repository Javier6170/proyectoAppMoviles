package com.android.busimap.bd

import com.android.busimap.activities.Posicion
import com.android.busimap.modelo.EstadoLugar
import com.android.busimap.modelo.Horario
import com.android.busimap.modelo.Lugar
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

object Lugares {
    private val lista:ArrayList<Lugar> = ArrayList()

    init {

        val horario1 = Horario(1, Horarios.obtenerTodos(), 12, 20)
        val horario2 = Horario(2, Horarios.obtenerEntreSemana(), 9, 23)
        val horario3 = Horario(3, Horarios.obtenerFinSemana(), 14, 23)

        val tels:ArrayList<String> = ArrayList()
        tels.add("7828789")
        tels.add("7828789")
/*
        val lugar1 = Lugar("Café ABC", "Excelente café para compartir", 1, EstadoLugar.ACEPTADO, 2, "Calle 123", Posicion(4.548988, -75.6615188), 1)
        //lugar1.id = 1
        lugar1.horarios.add(horario2)

        val lugar2 = Lugar("Bar City Pub", "Bar en la ciudad de Armenia", 2, EstadoLugar.ACEPTADO, 5, "Calle 12 # 23-1", Posicion(4.5499408, -75.6569629), 1)
        //lugar2.id = 2
        lugar2.telefonos = tels
        lugar2.horarios.add(horario1)

        val lugar3 = Lugar("Restaurante Mi Cuate", "Comida Mexicana para todos", 3, EstadoLugar.ACEPTADO, 3, "Calle 452", Posicion(4.5345584, -75.6783924), 5)
        //lugar3.id = 3
        lugar3.horarios.add(horario1)

        val lugar4 = Lugar("BBC Norte Pub", "Cervezas BBC muy buenas", 1, EstadoLugar.ACEPTADO, 5, "Calle 53", Posicion(4.5387929, -75.6753918), 3)
        //lugar4.id = 4
        lugar4.horarios.add(horario3)

        val lugar5 = Lugar("Hotel barato", "Hotel para ahorrar mucho dinero", , EstadoLugar.ACEPTADO, 1, "Calle 23 # 34-1", Posicion(4.5396581, -75.6727824), 1)
        //lugar5.id = 5
        lugar5.horarios.add( horario1 )

        val lugar6 = Lugar("Hostal Hippie", "Alojamiento para todos y todas", "2", EstadoLugar.SIN_REVISAR, 1, "Carrera 123", Posicion(4.5502249, -75.671664), 2)
       // lugar6.id = 6
        lugar6.horarios.add( horario2 )

        lista.add( lugar1 )
        lista.add( lugar2 )
        lista.add( lugar3 )
        lista.add( lugar4 )
        lista.add( lugar5 )
        lista.add( lugar6 )

 */

    }

    fun listarPorEstado(estado:EstadoLugar):ArrayList<Lugar>{
        return lista.filter { l -> l.estado == estado }.toCollection(ArrayList())
    }

    fun obtener(id:String): Lugar?{
        return lista.firstOrNull { l -> l.key == id }
    }



    fun buscarNombre(nombre:String): ArrayList<Lugar> {
        return lista.filter { l -> l.nombre.lowercase().contains(nombre.lowercase()) && l.estado == EstadoLugar.ACEPTADO }.toCollection(ArrayList())
    }

    fun crear(lugar:Lugar){
        //lugar.id = lista.size + 1
        lista.add( lugar )
    }

    fun buscarCiudad(codigoCiudad:Int): ArrayList<Lugar> {
        return lista.filter { l -> l.idCiudad == codigoCiudad && l.estado == EstadoLugar.ACEPTADO }.toCollection(ArrayList())
    }

    fun buscarCategoria(codigoCategoria:Int): ArrayList<Lugar> {
        return lista.filter { l -> l.idCategoria == codigoCategoria && l.estado == EstadoLugar.ACEPTADO }.toCollection(ArrayList())
    }

    fun listarPorPropietario(codigo:String):ArrayList<Lugar>{
        return lista.filter { l -> l.idCreador == codigo }.toCollection(ArrayList())
    }


/*
    fun cambiarEstado(codigo:String, nuevoEstado:EstadoLugar){

        val lugar = lista.firstOrNull{ l -> l.key == codigo}

        if(lugar!=null){
            lugar.estado = nuevoEstado
        }

    }


 */

}