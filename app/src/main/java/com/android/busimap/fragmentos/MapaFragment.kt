package com.android.busimap.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.busimap.R
import com.android.busimap.databinding.FragmentMapaBinding
import com.android.busimap.databinding.FragmentMisNegociosBinding


class MapaFragment : Fragment() {
    lateinit var binding: FragmentMapaBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapaBinding.inflate(inflater, container, false)

        return binding.root
    }
}