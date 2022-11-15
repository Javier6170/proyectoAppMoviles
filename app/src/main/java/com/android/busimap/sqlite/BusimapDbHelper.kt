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
            "create table ${UsuarioContrato.TABLE_NAME}( " +
                    "${UsuarioContrato.ID} INTEGER primary key AUTOINCREMENT, " +
                    "${UsuarioContrato.NOMBRE} varchar(100) not null, " +
                    "${UsuarioContrato.NICKNAME} varchar(100) not null unique," +
                    "${UsuarioContrato.CORREO} varchar(150) not null unique, " +
                    "${UsuarioContrato.PASSWORD} varchar(50) not null)"
        )

        db?.execSQL(
            "create table ${LugarContrato.TABLE_NAME}( " +
                    "${LugarContrato.ID} INTEGER primary key, " +
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
        db?.execSQL("drop table if exists ${UsuarioContrato.TABLE_NAME}")
        db?.execSQL("drop table if exists ${LugarContrato.TABLE_NAME}")
        onCreate(db)
    }

    @Throws(Exception::class)
    fun crearUsuario(usuario: Usuario) {
        try {
            writableDatabase.insert(
                UsuarioContrato.TABLE_NAME,
                null,
                usuario.toContentValues()
            )
        } catch (e: Exception) {
            throw Exception("${e.message}")
        }
    }

    @Throws(Exception::class)
    fun crearLugar(lugar: Lugar) {
        val lugarBuscado = obtenerLugar(lugar.id)
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

    fun actualizarUsuario(usuario: Usuario) {
        writableDatabase.update(
            UsuarioContrato.TABLE_NAME,
            usuario.toContentValues(),
            "${UsuarioContrato.ID} = ?",
            arrayOf(usuario.id.toString())
        )
    }

    fun eliminarUsuario(id: Int) {

        writableDatabase.delete(
            UsuarioContrato.TABLE_NAME,
            "${UsuarioContrato.ID} = ?",
            arrayOf(id.toString())
        )
    }

    fun listarUsuarios(): ArrayList<Usuario> {
        val lista: ArrayList<Usuario> = ArrayList()

        val c: Cursor = readableDatabase.query(
            UsuarioContrato.TABLE_NAME,
            arrayOf(
                UsuarioContrato.ID,
                UsuarioContrato.NOMBRE,
                UsuarioContrato.NICKNAME,
                UsuarioContrato.CORREO,
                UsuarioContrato.PASSWORD
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
                    Usuario(
                        c.getInt(0),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3),
                        c.getString(4)
                    )
                )
            } while (c.moveToNext())
        }

        return lista
    }


    fun listarLugares(): ArrayList<Lugar> {
        val lista: ArrayList<Lugar> = ArrayList()

        val c: Cursor = readableDatabase.query(
            LugarContrato.TABLE_NAME,
            arrayOf(
                LugarContrato.ID,
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
                        c.getInt(0),
                        c.getString(1),
                        c.getString(2),
                        c.getDouble(3),
                        c.getDouble(4),
                        c.getString(5),
                        c.getInt(6),
                        c.getInt(7)
                    )
                )
            } while (c.moveToNext())
        }

        return lista
    }

    fun obtenerUsuario(id: Int): Usuario? {
        var usuario: Usuario? = null

        val c: Cursor = readableDatabase.query(
            UsuarioContrato.TABLE_NAME,
            arrayOf(
                UsuarioContrato.ID,
                UsuarioContrato.NOMBRE,
                UsuarioContrato.NICKNAME,
                UsuarioContrato.CORREO,
                UsuarioContrato.PASSWORD
            ),
            "${UsuarioContrato.ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        if (c.moveToFirst()) {
            usuario =
                Usuario(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4))
        }

        return usuario
    }

    fun obtenerLugar(id: Int): Lugar? {
        var lugar: Lugar? = null

        val c: Cursor = readableDatabase.query(
            LugarContrato.TABLE_NAME,
            arrayOf(
                LugarContrato.ID,
                LugarContrato.NOMBRE,
                LugarContrato.DESCRIPCION,
                LugarContrato.LAT,
                LugarContrato.LNG,
                LugarContrato.DIRECCION,
                LugarContrato.CATEGORIA,
                LugarContrato.ID_CREADOR
            ),
            "${LugarContrato.ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        if (c.moveToFirst()) {
            lugar =
                Lugar(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getDouble(3),
                    c.getDouble(4),
                    c.getString(5),
                    c.getInt(6),
                    c.getInt(7)
                )
        }

        return lugar
    }


    fun comprobarDatos(correo: String, password: String): Usuario? {
        var usuario: Usuario? = null

        val c: Cursor = readableDatabase.query(
            UsuarioContrato.TABLE_NAME,
            arrayOf(
                UsuarioContrato.ID,
                UsuarioContrato.NOMBRE,
                UsuarioContrato.NICKNAME,
                UsuarioContrato.CORREO,
                UsuarioContrato.PASSWORD
            ),
            "${UsuarioContrato.CORREO} = ? and ${UsuarioContrato.PASSWORD} = ?",
            arrayOf(correo, password),
            null,
            null,
            null
        )

        if (c.moveToFirst()) {
            usuario =
                Usuario(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4))
        }

        return usuario
    }
}