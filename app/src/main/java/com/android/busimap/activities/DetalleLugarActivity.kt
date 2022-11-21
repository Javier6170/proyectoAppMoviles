package com.android.busimap.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.busimap.R
import com.android.busimap.adapter.ImagenesViewPager
import com.android.busimap.adapter.ViewPagerAdapter
import com.android.busimap.databinding.ActivityDetalleLugarBinding
import com.android.busimap.modelo.Lugar
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DetalleLugarActivity : AppCompatActivity() {

    private lateinit var binding:ActivityDetalleLugarBinding
    private var codigoLugar:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleLugarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        codigoLugar = intent.extras!!.getString("codigo","")

        if(codigoLugar != null) {

            binding.viewPager.adapter = ViewPagerAdapter(this, codigoLugar)
            TabLayoutMediator(binding.tabs, binding.viewPager) { tab, pos ->
                when (pos) {
                    0 -> tab.text = getString(R.string.info_lugar)
                    1 -> tab.text = getString(R.string.comentarios)
                }
            }.attach()

            Firebase.firestore
                .collection("lugares")
                .document(codigoLugar)
                .get()
                .addOnSuccessListener {
                    var lugarF = it.toObject(Lugar::class.java)
                    if (lugarF!= null){
                        binding.listaImagenes.adapter = ImagenesViewPager(this, lugarF.imagenes)
                    }
                }
        }
    }
}