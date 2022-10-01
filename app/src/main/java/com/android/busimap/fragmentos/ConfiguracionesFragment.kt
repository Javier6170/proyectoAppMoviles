package com.android.busimap.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.busimap.R
import com.android.busimap.databinding.FragmentConfiguracionesBinding
import com.android.busimap.databinding.FragmentFavoritosBinding


class ConfiguracionesFragment : Fragment() {

    lateinit var binding: FragmentConfiguracionesBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfiguracionesBinding.inflate(inflater, container, false)

        return binding.root
    }
}