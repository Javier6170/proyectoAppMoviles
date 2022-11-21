package com.android.busimap.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.busimap.R
import com.android.busimap.databinding.FragmentConfiguracionesBinding
import com.android.busimap.databinding.FragmentFavoritosBinding
import com.android.busimap.modelo.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ConfiguracionesFragment : Fragment() {

    lateinit var binding: FragmentConfiguracionesBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfiguracionesBinding.inflate(inflater, container, false)


        var user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            binding.root.findViewById<TextView>(R.id.txt_nombreUserN).text = user.email
            Firebase.firestore
                .collection("usuarios")
                .document(user.uid)
                .get()
                .addOnSuccessListener { u ->
                    val nickname = u.toObject(Usuario::class.java)?.nickname
                    binding.root.findViewById<TextView>(R.id.txt_nickUserN).text = nickname
                }



        }
        return binding.root
    }

}