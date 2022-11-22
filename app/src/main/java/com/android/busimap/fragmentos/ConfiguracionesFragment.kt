package com.android.busimap.fragmentos

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.android.busimap.R
import com.android.busimap.databinding.FragmentConfiguracionesBinding
import com.android.busimap.modelo.Usuario
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*


class ConfiguracionesFragment : Fragment() {

    lateinit var binding: FragmentConfiguracionesBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var codigoArchivo = 0
    var imagenes: ArrayList<String> = ArrayList()
    lateinit var dialog: Dialog
    var user: FirebaseUser? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfiguracionesBinding.inflate(inflater, container, false)




        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setView(R.layout.dialogo_progreso)
        dialog = builder.create()


        user = FirebaseAuth.getInstance().currentUser


        if (user != null) {

            Firebase.firestore.collection("usuarios")
                .document(user!!.uid)
                .get()
                .addOnSuccessListener {
                    val userF = it.toObject(Usuario::class.java)

                    var imagen = userF!!.imagenUser

                    if (imagen!=""){
                        binding.seleccionarFoto.visibility = View.INVISIBLE

                        Glide.with(binding.root.context)
                            .load(imagen)
                            .into(binding.imagenUsuario)

                    }else{
                        binding.seleccionarFoto.visibility = View.VISIBLE
                    }
                }

            binding.root.findViewById<TextView>(R.id.txt_nombreUserN).text = user!!.email
            Firebase.firestore
                .collection("usuarios")
                .document(user!!.uid)
                .get()
                .addOnSuccessListener { u ->
                    val nickname = u.toObject(Usuario::class.java)?.nickname
                    binding.root.findViewById<TextView>(R.id.txt_nickUserN).text = nickname
                }
        }

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            onActivityResult(it.resultCode, it)
        }

        binding.btnTomarFoto.setOnClickListener { tomarFoto() }

        return binding.root
    }

    fun tomarFoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                resultLauncher.launch(takePictureIntent)
                codigoArchivo = 1
            }
        }
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
            }
        }
    }

    fun dibujarImagen(url: Uri) {

        var imagen = ImageView(requireActivity().baseContext)
        imagen.layoutParams = LinearLayout.LayoutParams(300, 310)
        Glide.with(requireActivity().baseContext)
            .load(url.toString())
            .into(imagen)
        imagenes.add(url.toString())

        Firebase.firestore
            .collection("usuarios")
            .document(user!!.uid)
            .update("imagenUser", imagenes.first())
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Firebase.firestore
                        .collection("usuarios")
                        .document(user!!.uid)
                        .get()
                        .addOnSuccessListener {
                            var userF = it.toObject(Usuario::class.java)
                            Glide.with(binding.root.context)
                                .load(userF!!.imagenUser)
                                .into(binding.imagenUsuario)
                        }
                }
            }


    }

    private fun setDialog(show: Boolean) {
        if (show) dialog.show() else dialog.dismiss()
    }

}