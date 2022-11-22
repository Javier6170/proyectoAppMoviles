package com.android.busimap.sqlite

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.android.busimap.modelo.Lugar
import com.android.busimap.modelo.Usuario

class BusimapDbHelper(context: Context) : SQLiteOpenHelper(context, "usuarios.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL(
            "create table ${LugarContrato.TABLE_NAME}( " +
                    "${LugarContrato.ID} INTEGER PRIMARY KEY, " +
                    "${LugarContrato.NOMBRE} varchar(100) not null, " +
                    "${LugarContrato.DESCRIPCION} varchar(100) not null," +
                    "${LugarContrato.LAT} double not null, " +
                    "${LugarContrato.LNG} double not null," +
                    "${LugarContrato.DIRECCION} varchar(200) not null," +
                    "${LugarContrato.CATEGORIA} INTEGER not null," +
                    "${LugarContrato.ID_CREADOR} INTEGER not null)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists ${LugarContrato.TABLE_NAME}")
        onCreate(db)
    }


    @Throws(Exception::class)
    fun crearLugar(lugar: Lugar) {
        val lugarBuscado = obtenerLugar(lugar.idCreador)
        if (lugarBuscado == null) {
            try {

                writableDatabase.insert(
                    LugarContrato.TABLE_NAME,
                    null,
                    lugar.toContentValues()
                )

            } catch (e: Exception) {
                throw Exception("${e.message}")
            }
        }
    }


    fun listarLugares(): ArrayList<Lugar> {
        val lista: ArrayList<Lugar> = ArrayList()

        val c: Cursor = readableDatabase.query(
            LugarContrato.TABLE_NAME,
            arrayOf(
                LugarContrato.NOMBRE,
                LugarContrato.DESCRIPCION,
                LugarContrato.LAT,
                LugarContrato.LNG,
                LugarContrato.DIRECCION,
                LugarContrato.CATEGORIA,
                LugarContrato.ID_CREADOR

            ),
            null,
            null,
            null,
            null,
            null
        )

        if (c.moveToFirst()) {
            do {
                lista.add(
                    Lugar(
                        c.getString(1),
                        c.getString(2),
                        c.getDouble(3),
                        c.getDouble(4),
                        c.getString(5),
                        c.getInt(6),
                        c.getString(7)
                    )
                )
            } while (c.moveToNext())
        }

        return lista
    }



    fun obtenerLugar(id: String): Lugar? {
        var lugar: Lugar? = null

        val c: Cursor = readableDatabase.query(
            LugarContrato.TABLE_NAME,
            arrayOf(
                LugarContrato.NOMBRE,
                LugarContrato.DESCRIPCION,
                LugarContrato.LAT,
                LugarContrato.LNG,
                LugarContrato.DIRECCION,
                LugarContrato.CATEGORIA,
                LugarContrato.ID_CREADOR
            ),
            "${LugarContrato.ID_CREADOR} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        if (c.moveToFirst()) {
            lugar =
                Lugar(
                    c.getString(1),
                    c.getString(2),
                    c.getDouble(3),
                    c.getDouble(4),
                    c.getString(5),
                    c.getInt(6),
                    c.getString(7)
                )
        }

        return lugar
    }
}