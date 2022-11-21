package com.android.busimap.fragmentos

import android.graphics.Canvas
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.busimap.R
import com.android.busimap.adapter.LugarAdapter
import com.android.busimap.bd.Lugares
import com.android.busimap.databinding.FragmentComentariosBinding
import com.android.busimap.databinding.FragmentListaLugaresModeradorBinding
import com.android.busimap.modelo.EstadoLugar
import com.android.busimap.modelo.Lugar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class ListaLugaresModeradorFragment : Fragment() {

    lateinit var binding: FragmentListaLugaresModeradorBinding
    lateinit var adapterLista: LugarAdapter
    lateinit var listaLugares: ArrayList<Lugar>
    var lugar: Lugar = Lugar()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Firebase.firestore
            .collection("lugares")
            .get()
            .addOnSuccessListener {
                listaLugares = it.toObjects(Lugar::class.java) as ArrayList<Lugar>
            }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListaLugaresModeradorBinding.inflate(inflater, container, false)

        adapterLista = LugarAdapter(listaLugares)
        binding.listaLugares.adapter = adapterLista
        binding.listaLugares.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        val simpleCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var pos = viewHolder.adapterPosition
                val codigoLugar = listaLugares[pos].key


                Firebase.firestore
                    .collection("lugares")
                    .document(codigoLugar)
                    .get()
                    .addOnSuccessListener {
                        lugar = it.toObject(Lugar::class.java)!!
                    }

                when (direction) {
                    ItemTouchHelper.LEFT -> {

                        Firebase.firestore
                            .collection("lugares")
                            .document(codigoLugar)
                            .update("estado", EstadoLugar.ACEPTADO)
                            .addOnSuccessListener {
                                adapterLista.notifyItemRemoved(pos)
                                Snackbar.make(
                                    binding.listaLugares,
                                    "Lugar aceptado!",
                                    Snackbar.LENGTH_LONG
                                )
                                    .setAction("Deshacer", View.OnClickListener {
                                        Firebase.firestore
                                            .collection("lugares")
                                            .document(codigoLugar)
                                            .update("estado", EstadoLugar.SIN_REVISAR)
                                            .addOnSuccessListener {
                                                listaLugares.add(pos, lugar!!)
                                                adapterLista.notifyItemInserted(pos)
                                            }
                                    }).show()
                            }
                    }
                    ItemTouchHelper.RIGHT -> {

                        Firebase.firestore
                            .collection("lugares")
                            .document(codigoLugar)
                            .update("estado", EstadoLugar.RECHAZADO)
                            .addOnSuccessListener {
                                adapterLista.notifyItemRemoved(pos)
                                Snackbar.make(
                                    binding.listaLugares,
                                    "Lugar rechazado!",
                                    Snackbar.LENGTH_LONG
                                )
                                    .setAction("Deshacer", View.OnClickListener {
                                        Firebase.firestore
                                            .collection("lugares")
                                            .document(codigoLugar)
                                            .update("estado", EstadoLugar.SIN_REVISAR)
                                            .addOnSuccessListener {
                                                listaLugares.add(pos, lugar!!)
                                                adapterLista.notifyItemInserted(pos)
                                            }
                                        listaLugares.add(pos, lugar!!)
                                        adapterLista.notifyItemInserted(pos)
                                    }).show()
                            }


                    }

                }


            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )

                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addSwipeLeftBackgroundColor(
                        ContextCompat.getColor(
                            activity!!.baseContext,
                            R.color.color_green_light
                        )
                    )
                    .addSwipeRightBackgroundColor(
                        ContextCompat.getColor(
                            activity!!.baseContext,
                            R.color.red
                        )
                    )
                    .addSwipeLeftLabel("Acceptor")
                    .addSwipeRightLabel("Rechazar")
                    .create()
                    .decorate()
            }

        }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.listaLugares)



        return binding.root
    }

    companion object {

    }
}