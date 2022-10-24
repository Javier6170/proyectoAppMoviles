package com.android.busimap.modelo

import com.android.busimap.activities.Posicion
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class Lugar(var nombre:String,
            var descripcion:String,
            var idCreador:Int,
            var estado:EstadoLugar,
            var idCategoria:Int,
            var direccion:String,
            var posicion: Posicion,
            var idCiudad:Int
) {

    var id:Int = 0
    var imagenes:ArrayList<String> = ArrayList()
    var telefonos:ArrayList<String> = ArrayList()
    var fecha: Date = Date()
    var horarios:ArrayList<Horario> = ArrayList()

    fun estaAbierto():Boolean{

        val fecha = Calendar.getInstance()
        val dia = fecha.get(Calendar.DAY_OF_WEEK)
        val hora = fecha.get(Calendar.HOUR_OF_DAY)
        //val minuto = fecha.get(Calendar.MINUTE)

        var mensaje = false

        for(horario in horarios){
            if( horario.diaSemana.contains( DiaSemana.values()[dia-1] ) && hora < horario.horaCierre && hora > horario.horaInicio  ){
                mensaje = true
                break
            }
        }

        return mensaje
    }

    fun obtenerHoraCierre():String{

        val fecha = Calendar.getInstance()
        val dia = fecha.get(Calendar.DAY_OF_WEEK)

        var mensaje = ""

        for(horario in horarios){
            if( horario.diaSemana.contains( DiaSemana.values()[dia-1] ) ){
                mensaje = "${horario.horaCierre}:00"
                break
            }
        }

        return mensaje
    }

    fun obtenerHoraApertura():String{

        val fecha = Calendar.getInstance()
        val dia = fecha.get(Calendar.DAY_OF_WEEK)
        val hora = fecha.get(Calendar.HOUR_OF_DAY)

        var mensaje = ""
        var pos:Int

        for(horario in horarios){
            pos = horario.diaSemana.indexOf( DiaSemana.values()[dia-1] )
            mensaje = if( pos!=-1 ){
                if( horario.horaInicio > hora ){
                    "${horario.diaSemana[pos].toString().lowercase()} a las ${horario.horaInicio}:00"
                }else{
                    "${horario.diaSemana[pos+1].toString().lowercase()} a las ${horario.horaInicio}:00"
                }
            }else{
                "${horario.diaSemana[0].toString().lowercase()} a las ${horario.horaInicio}:00"
            }
            break
        }

        return mensaje
    }

    fun obtenerCalificacionPromedio(comentarios:ArrayList<Comentario>):Int{
        var promedio = 0

        if(comentarios.size > 0) {
            val suma = comentarios.stream().map { c -> c.calificacion }
                .reduce { suma, valor -> suma + valor }.get()

            promedio = suma/comentarios.size
        }

        return promedio
    }

    override fun toString(): String {
        return "Lugar(id=$id, nombre='$nombre', descripcion='$descripcion', idCreador=$idCreador, estado=$estado, idCategoria=$idCategoria, posicion=$posicion, idCiudad=$idCiudad, imagenes=$imagenes, telefonos=$telefonos, fecha=$fecha, horarios=$horarios)"
    }
}