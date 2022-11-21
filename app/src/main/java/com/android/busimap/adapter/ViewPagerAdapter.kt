package com.android.busimap.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.busimap.fragmentos.ComentariosFragment
import com.android.busimap.fragmentos.InfoLugarFragment

class ViewPagerAdapter(var fragment:FragmentActivity, private var codigoLugar:String): FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> InfoLugarFragment.newInstance(codigoLugar)
            else -> ComentariosFragment.newInstance(codigoLugar)
        }
    }
}