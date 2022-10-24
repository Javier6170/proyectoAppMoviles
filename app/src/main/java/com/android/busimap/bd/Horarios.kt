package com.android.busimap.bd

import com.android.busimap.modelo.DiaSemana
import com.android.busimap.modelo.Horario

object Horarios {
    val lista: ArrayList<Horario> = ArrayList()

    fun obtenerTodos():ArrayList<DiaSemana>{
        val todosDias: ArrayList<DiaSemana> = ArrayList()
        todosDias.addAll(DiaSemana.values())
        return todosDias
    }

    fun obtenerFinSemana(): ArrayList<DiaSemana>{
        val todosDias: ArrayList<DiaSemana> = ArrayList()
        todosDias.add(DiaSemana.VIERNES)
        todosDias.add(DiaSemana.SABADO)
         return todosDias
    }

    fun obtenerEntreSemana(): ArrayList<DiaSemana>{
        val todosDias: ArrayList<DiaSemana> = ArrayList()
        todosDias.add(DiaSemana.LUNES)
        todosDias.add(DiaSemana.MARTES)
        todosDias.add(DiaSemana.MIERCOLES)
        todosDias.add(DiaSemana.JUEVES)
        todosDias.add(DiaSemana.VIERNES)
        return todosDias
    }
    fun agregarHorario(horario:Horario):Horario{
        horario.id = lista.size + 1
        lista.add( horario )
        return horario
    }
}