package com.android.busimap.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.busimap.R
import com.android.busimap.databinding.FragmentAyudaBinding
import com.android.busimap.databinding.FragmentConfiguracionesBinding

class AyudaFragment : Fragment() {
    lateinit var binding: FragmentAyudaBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAyudaBinding.inflate(inflater, container, false)

        return binding.root
    }
}