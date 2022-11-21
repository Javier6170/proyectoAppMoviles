package com.android.busimap.activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.android.busimap.R
import com.android.busimap.databinding.ActivityVistaCrearNegocioBinding
import com.android.busimap.fragmentos.crearLugar.HorariosDialogoFragment
import com.android.busimap.modelo.*
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.shuhart.stepview.StepView
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class VistaCrearNegocio : AppCompatActivity(), HorariosDialogoFragment.OnHorarioCreadoListener,
    OnMapReadyCallback {

    lateinit var stepView: StepView
    lateinit var stepTextView: TextView
    lateinit var descriptionTextView: TextView


    var stepIndex = 0
    var stepTexts: ArrayList<String> = ArrayList()
    var descriptionTexts: ArrayList<String> = ArrayList()
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    lateinit var binding: ActivityVistaCrearNegocioBinding
    var posCiudad: Int = -1
    var posCategoria: Int = -1
    lateinit var ciudades: ArrayList<Ciudad>
    lateinit var categorias: ArrayList<Categoria>
    lateinit var horarios: ArrayList<Horario>
    lateinit var gMap: GoogleMap
    private var tienePermiso = false
    private val defaultLocation = LatLng(4.550923, -75.6557201)
    private var posicion: Posicion? = null
    private var codigoArchivo = 0
    var nuevoLugar: Lugar? = null
    var imagenes: ArrayList<String> = ArrayList()
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVistaCrearNegocioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(R.layout.dialogo_progreso)
        dialog = builder.create()


        stepTexts.add("Paso 1")
        stepTexts.add("Paso 2")
        stepTexts.add("Paso 3")
        stepTexts.add("Paso 4")
        stepTexts.add("Paso 5")

        descriptionTexts.add("Digita la informacion del local")
        descriptionTexts.add("Escoge la imagen de tu negocio")
        descriptionTexts.add("Organiza los horarios de tu local")
        descriptionTexts.add("Elige en que parte del mapa estara tu local")
        descriptionTexts.add("Presiona el boton para completar la información")

        stepTextView = binding.stepTextView
        descriptionTextView = binding.descriptionTview
        stepView = binding.stepView

        stepView
            .state
            .animationType(StepView.ANIMATION_ALL)
            .stepsNumber(5)
            .animationDuration(resources.getInteger(android.R.integer.config_shortAnimTime))
            .commit()
        stepTextView.text = stepTexts[stepIndex]
        descriptionTextView.text = descriptionTexts[stepIndex]

        if (stepIndex == 0) {
            binding.btnAnteriorStep.visibility = View.INVISIBLE
            binding.linearAdministrarMapa.visibility = View.INVISIBLE
            binding.linearOrganizarHorarios.visibility = View.INVISIBLE
            binding.linearGuardarInformacion.visibility = View.INVISIBLE
            binding.linearSeleccionarImagen.visibility = View.INVISIBLE
        }


        horarios = ArrayList()
        ciudades = ArrayList()
        categorias = ArrayList()


        Firebase.firestore
            .collection("categorias")
            .get()
            .addOnSuccessListener {
                for (dec in it) {
                    categorias.add(dec.toObject(Categoria::class.java))
                }
                cargarCategorias()
            }

        Firebase.firestore
            .collection("ciudades")
            .get()
            .addOnSuccessListener {
                for (dec in it) {
                    ciudades.add(dec.toObject(Ciudad::class.java))
                }
                cargarCiudades()
            }

        binding.btnCrearLugar.setOnClickListener { crearNuevoLugar() }

        binding.btnSiguienteStep.setOnClickListener { nextStep() }

        binding.btnMostrarHorario.setOnClickListener { mostrarDialogo() }

        binding.btnAnteriorStep.setOnClickListener { anteriorStep() }

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapa_crear_lugar) as SupportMapFragment
        mapFragment.getMapAsync(this)

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            onActivityResult(it.resultCode, it)
        }


        binding.btnTomarFoto.setOnClickListener { tomarFoto() }
        binding.btnSelArchivo.setOnClickListener { seleccionarArchivo() }
    }

    fun tomarFoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                resultLauncher.launch(takePictureIntent)
                codigoArchivo = 1
            }
        }

    }

    fun seleccionarArchivo() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        codigoArchivo = 2
        resultLauncher.launch(i)

    }

    private fun onActivityResult(resultCode: Int, result: ActivityResult) {
        setDialog(true)
        if (resultCode == Activity.RESULT_OK) {
            val fecha = Date()
            val storageRef = FirebaseStorage.getInstance()
                .reference
                .child("/p-${fecha.time}.jpg")
            if (codigoArchivo == 1) {
                val data = result.data?.extras
                if (data?.get("data") is Bitmap) {
                    val imageBitmap = data?.get("data") as Bitmap
                    val baos = ByteArrayOutputStream()
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()
                    storageRef.putBytes(data).addOnSuccessListener {
                        storageRef.downloadUrl.addOnSuccessListener {
                            dibujarImagen(it)
                            setDialog(false)
                        }
                    }.addOnFailureListener {
                        Snackbar.make(binding.root, "${it.message}", Snackbar.LENGTH_LONG).show()
                        setDialog(false)
                    }
                }

            } else if (codigoArchivo == 2) {
                val data = result.data
                if(data!=null){
                    val selectedImageUri: Uri? = data.data
                    if(selectedImageUri!=null){
                        storageRef.putFile(selectedImageUri).addOnSuccessListener {
                            storageRef.downloadUrl.addOnSuccessListener {
                                dibujarImagen(it)
                                setDialog(false)
                            }
                        }.addOnFailureListener {
                            Snackbar.make(binding.root, "${it.message}", Snackbar.LENGTH_LONG).show()
                            setDialog(false)
                        }
                    }
                }

            }
        }
    }

    private fun setDialog(show: Boolean) {
        if (show) dialog.show() else dialog.dismiss()
    }


    fun dibujarImagen(url: Uri) {

        var imagen = ImageView(baseContext)
        imagen.layoutParams = LinearLayout.LayoutParams(300, 310)
        binding.imagenesSeleccionadas.addView(imagen)
        Glide.with( baseContext )
            .load(url.toString())
            .into(imagen)
        imagenes.add(url.toString())
    }

    fun anteriorStep() {

        stepIndex--
        if (stepIndex > 0) {
            binding.btnAnteriorStep.visibility = View.VISIBLE
        } else {
            binding.btnAnteriorStep.visibility = View.INVISIBLE
        }
        when (stepIndex) {
            0 -> {
                binding.linearInfoNegocio.visibility = View.VISIBLE
                binding.linearAdministrarMapa.visibility = View.INVISIBLE
                binding.linearSeleccionarImagen.visibility = View.INVISIBLE
                binding.linearOrganizarHorarios.visibility = View.INVISIBLE
                binding.linearGuardarInformacion.visibility = View.INVISIBLE
            }
            1 -> {
                binding.linearInfoNegocio.visibility = View.INVISIBLE
                binding.linearSeleccionarImagen.visibility = View.VISIBLE
                binding.linearAdministrarMapa.visibility = View.INVISIBLE
                binding.linearOrganizarHorarios.visibility = View.INVISIBLE
                binding.linearGuardarInformacion.visibility = View.INVISIBLE
            }
            2 -> {
                binding.linearInfoNegocio.visibility = View.INVISIBLE
                binding.linearAdministrarMapa.visibility = View.INVISIBLE
                binding.linearSeleccionarImagen.visibility = View.INVISIBLE
                binding.linearOrganizarHorarios.visibility = View.VISIBLE
                binding.linearGuardarInformacion.visibility = View.INVISIBLE
            }
            3 -> {
                binding.linearInfoNegocio.visibility = View.INVISIBLE
                binding.linearAdministrarMapa.visibility = View.VISIBLE
                binding.linearOrganizarHorarios.visibility = View.INVISIBLE
                binding.linearSeleccionarImagen.visibility = View.INVISIBLE
                binding.linearGuardarInformacion.visibility = View.INVISIBLE
            }
            else -> {
                binding.linearInfoNegocio.visibility = View.INVISIBLE
                binding.linearAdministrarMapa.visibility = View.INVISIBLE
                binding.linearSeleccionarImagen.visibility = View.INVISIBLE
                binding.linearOrganizarHorarios.visibility = View.INVISIBLE
                binding.linearGuardarInformacion.visibility = View.VISIBLE
            }
        }
        if (stepIndex < stepTexts.size) {
            stepTextView.text = stepTexts[stepIndex]
            descriptionTextView.text = descriptionTexts[stepIndex]
            stepView.go(stepIndex, true)
        }

    }

    fun nextStep() {

        stepIndex++
        if (stepIndex > 0) {
            binding.btnAnteriorStep.visibility = View.VISIBLE
        } else {
            binding.btnAnteriorStep.visibility = View.INVISIBLE
        }

        when (stepIndex) {
            0 -> {
                binding.linearInfoNegocio.visibility = View.VISIBLE
                binding.linearAdministrarMapa.visibility = View.INVISIBLE
                binding.linearSeleccionarImagen.visibility = View.INVISIBLE
                binding.linearOrganizarHorarios.visibility = View.INVISIBLE
                binding.linearGuardarInformacion.visibility = View.INVISIBLE
            }
            1 -> {
                binding.linearInfoNegocio.visibility = View.INVISIBLE
                binding.linearSeleccionarImagen.visibility = View.VISIBLE
                binding.linearAdministrarMapa.visibility = View.INVISIBLE
                binding.linearOrganizarHorarios.visibility = View.INVISIBLE
                binding.linearGuardarInformacion.visibility = View.INVISIBLE
            }
            2 -> {
                binding.linearInfoNegocio.visibility = View.INVISIBLE
                binding.linearAdministrarMapa.visibility = View.INVISIBLE
                binding.linearSeleccionarImagen.visibility = View.INVISIBLE
                binding.linearOrganizarHorarios.visibility = View.VISIBLE
                binding.linearGuardarInformacion.visibility = View.INVISIBLE
            }
            3 -> {
                binding.linearInfoNegocio.visibility = View.INVISIBLE
                binding.linearAdministrarMapa.visibility = View.VISIBLE
                binding.linearOrganizarHorarios.visibility = View.INVISIBLE
                binding.linearSeleccionarImagen.visibility = View.INVISIBLE
                binding.linearGuardarInformacion.visibility = View.INVISIBLE
            }
            else -> {
                binding.linearInfoNegocio.visibility = View.INVISIBLE
                binding.linearAdministrarMapa.visibility = View.INVISIBLE
                binding.linearSeleccionarImagen.visibility = View.INVISIBLE
                binding.linearOrganizarHorarios.visibility = View.INVISIBLE
                binding.linearGuardarInformacion.visibility = View.VISIBLE
            }
        }

        if (stepIndex < stepTexts.size) {
            stepTextView.text = stepTexts[stepIndex]
            descriptionTextView.text = descriptionTexts[stepIndex]
            stepView.go(stepIndex, true)
        }


    }


    fun mostrarDialogo() {

        val dialog = HorariosDialogoFragment()
        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogoTitulo)
        dialog.listener = this
        dialog.show(supportFragmentManager, "Agregar")

    }

    fun cargarCiudades() {

        var lista = ciudades.map { c -> c.nombre }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, lista)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.ciudadLugar.adapter = adapter

        binding.ciudadLugar.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                posCiudad = p2
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    fun cargarCategorias() {
        var lista = categorias.map { c -> c.nombre }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, lista)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categoriaLugar.adapter = adapter

        binding.categoriaLugar.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    posCategoria = p2
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
    }

    fun crearNuevoLugar() {
        setDialog(true)
        val nombre = binding.nombreLugar.text.toString()
        val descripcion = binding.descripcionLugar.text.toString()
        val telefono = binding.telefonoLugar.text.toString()
        val direccion = binding.direccionLugar.text.toString()
        val idCiudad = ciudades[posCiudad].id
        val idCategoria = categorias[posCategoria].id

        if (nombre.isEmpty()) {
            binding.nombreLayout.error = getString(R.string.es_obligatorio)
        } else {
            binding.nombreLayout.error = null
        }

        if (descripcion.isEmpty()) {
            binding.descripcionLayout.error = getString(R.string.es_obligatorio)
        } else {
            binding.descripcionLayout.error = null
        }

        if (direccion.isEmpty()) {
            binding.direccionLayout.error = getString(R.string.es_obligatorio)
        } else {
            binding.direccionLayout.error = null
        }

        if (telefono.isEmpty()) {
            binding.telefonoLayout.error = getString(R.string.es_obligatorio)
        } else {
            binding.telefonoLayout.error = null
        }

        if (nombre.isNotEmpty() && descripcion.isNotEmpty() && telefono.isNotEmpty() && direccion.isNotEmpty() && horarios.isNotEmpty() && idCiudad != -1 && idCategoria != -1) {

            if (imagenes.isNotEmpty()) {
                if (posicion != null) {
                    val user = FirebaseAuth.getInstance()
                    nuevoLugar =
                        Lugar(
                            nombre,
                            descripcion,
                            user.uid!!,
                            EstadoLugar.SIN_REVISAR,
                            idCategoria,
                            direccion,
                            posicion!!,
                            idCiudad
                        )


                    val telefonos: ArrayList<String> = ArrayList()
                    telefonos.add(telefono)

                    if (nuevoLugar != null) {
                        nuevoLugar!!.telefonos = telefonos
                        nuevoLugar!!.horarios = horarios
                        nuevoLugar!!.imagenes = imagenes

                        stepIndex++
                        if (stepIndex < stepTexts.size) {
                            stepTextView.text = stepTexts[stepIndex]
                            descriptionTextView.text = descriptionTexts[stepIndex]
                            stepView.go(stepIndex, true)
                        }
                        setDialog(false)
                        Firebase.firestore
                            .collection("lugares")
                            .add(nuevoLugar!!)
                            .addOnSuccessListener {
                                Snackbar.make(
                                    binding.root,
                                    getString(R.string.lugar_creado),
                                    Snackbar.LENGTH_LONG
                                ).show()

                                Handler(Looper.getMainLooper()).postDelayed(
                                    {
                                        finish()
                                    }, 2000
                                )
                            }
                            .addOnFailureListener {
                                Snackbar.make(binding.root, "${it.message}", Snackbar.LENGTH_LONG)
                                    .show()
                                setDialog(false)
                            }
                    }

                } else {
                    Snackbar.make(binding.root, getString(R.string.mensaje_mapa), Toast.LENGTH_LONG)
                        .show()
                    setDialog(false)
                }
            }else{
                Snackbar.make(binding.root, "Debes colocar una imagen a tu negocio!", Toast.LENGTH_LONG)
                    .show()
                setDialog(false)
            }


        } else {
            Snackbar.make(binding.root, getString(R.string.todos_obligatorios), Toast.LENGTH_LONG)
                .show()
            setDialog(false)
        }

    }

    override fun elegirHorario(horario: Horario) {
        horario.diaSemana.forEach {
            val textView = TextView(this)
            textView.text = "${
                it.name.lowercase().replaceFirstChar { c -> c.uppercase() }
            }  ${horario.horaInicio}:00 - ${horario.horaCierre}:00 "
            binding.listaHorarios.addView(textView)
        }

        binding.espacioHorarios.visibility = View.VISIBLE

        horarios.add(horario)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        gMap.uiSettings.isZoomControlsEnabled = true

        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f))

        gMap.setOnMapClickListener {

            if (posicion == null) {
                posicion = Posicion()
            }

            posicion!!.lat = it.latitude
            posicion!!.lng = it.longitude

            gMap.clear()
            gMap.addMarker(MarkerOptions().position(it))
        }
    }

}