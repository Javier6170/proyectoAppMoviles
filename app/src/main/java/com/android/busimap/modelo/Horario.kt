package com.android.busimap.modelo

import java.time.LocalDate

class Horario( ) {

    constructor( diaSemana: ArrayList<DiaSemana>,  horaInicio: Int,  horaCierre:Int): this(){
        this.diaSemana = diaSemana
        this.horaInicio = horaInicio
        this.horaCierre = horaCierre
    }

    var diaSemana: ArrayList<DiaSemana> = ArrayList()
    var horaInicio: Int = 0
    var horaCierre:Int = 0
    var key: String = ""
    var id:Int = 0

    constructor(id:Int, diaSemana:ArrayList<DiaSemana>, horaInicio:Int, horaCierre:Int):this(diaSemana, horaInicio, horaCierre){
        this.id = id
    }

    override fun toString(): String {
        return "Horario(diaSemana=$diaSemana, horaInicio=$horaInicio, horaCierre=$horaCierre, id=$id)"
    }

}