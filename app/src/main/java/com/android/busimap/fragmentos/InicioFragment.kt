package com.android.busimap.fragmentos

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.android.busimap.R
import com.android.busimap.activities.ResultadoBusqueda
import com.android.busimap.databinding.FragmentInicioBinding


class InicioFragment : Fragment() {
    lateinit var binding: FragmentInicioBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInicioBinding.inflate(inflater, container, false)

        binding.textoBusqueda.setOnEditorActionListener { textView, i, keyEvent ->
            if( i == EditorInfo.IME_ACTION_SEARCH){

                val busqueda = binding.textoBusqueda.text.toString()

                if(busqueda.isNotEmpty()) {
                    val intent = Intent(activity, ResultadoBusqueda::class.java)
                    intent.putExtra("texto", busqueda)
                    startActivity(intent)
                }

            }
            true
        }


        return binding.root
    }

}