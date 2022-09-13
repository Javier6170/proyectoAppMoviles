package com.android.busimap.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.busimap.R

class MainActivity : AppCompatActivity() {


    /**
     * Lo que pasa es que usted estaba cargando el layout en el main activity pero la funcionalidad de los botones no está acá
     * sino en el loginactivity por eso no funcionaba nada.
     *
     * Si usted quiere que una actividad se muestre de una al momento de ejecutar la app debe ir al manifest y ponerle el filtro Main y launcher
     *
     * listo profe gracias, y si usted habia mostrado eso pero bueno
     *
     * Lisyo, cualquier cosa me escribe
     *
     * profe es que yo estaba tratando de cambiar los colores de un boton pero no me funcionaba con el background eso no se puede ?
     *
     *
     * debe crear un archivo xml con el color y la forma que quiere y luego le asigna ese archivo como background
     *
     * es que ya lo habia hecho pero seguia sin funcionar
     *
     * lo que pasa es que por defecto se usa un tema de material design y este tema ya trae unas configuraciones predefinidas. Una opciión es
     * quitar este tema desde el archivo themes.xml y luego ya usted tiene libertad absoluta sobre todos los estilos de la app
     *
     * listo profe, gracias, uno no puede editar los colores de ese tema? pues porque esta usando como una paleta y no tener que crear todo desde 0
     *
     * sii, se puede, puede quitar esos colores que están en el archivo colors.xml y ya los va asignado en el thema, en las propiedades de
     * color primary, color secundary, colos accent, etc..
     *
     * listo profe eso era todo gracias
     *
     * listo
     *
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}