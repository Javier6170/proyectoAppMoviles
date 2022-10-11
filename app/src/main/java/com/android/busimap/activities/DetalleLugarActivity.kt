package com.android.busimap.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.busimap.R
import com.android.busimap.adapter.ViewPagerAdapter
import com.android.busimap.bd.Categorias
import com.android.busimap.bd.Lugares
import com.android.busimap.databinding.ActivityDetalleLugarBinding
import com.android.busimap.modelo.Lugar
import com.google.android.material.tabs.TabLayoutMediator

class DetalleLugarActivity : AppCompatActivity() {

    private lateinit var binding:ActivityDetalleLugarBinding
    private var codigoLugar:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleLugarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        codigoLugar = intent.extras!!.getInt("codigo")

        if(codigoLugar != 0) {

            binding.viewPager.adapter = ViewPagerAdapter(this, codigoLugar)
            TabLayoutMediator(binding.tabs, binding.viewPager) { tab, pos ->
                when (pos) {
                    0 -> tab.text = getString(R.string.info_lugar)
                    1 -> tab.text = getString(R.string.comentarios)
                }
            }.attach()

        }
    }
}