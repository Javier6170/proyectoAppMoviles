package com.android.busimap.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.busimap.R
import com.android.busimap.databinding.ActivityRegistroBinding

class RegistroActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}