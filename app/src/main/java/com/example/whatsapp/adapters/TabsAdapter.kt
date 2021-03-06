package com.example.whatsapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.whatsapp.ui.ui.fragments.CallsFragment
import com.example.whatsapp.ui.fragments.ChatsFragment
import com.example.whatsapp.ui.ui.fragments.StatusFragment

class TabsAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {



    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> return ChatsFragment()
            1 -> return StatusFragment()
            else -> return CallsFragment()
//            else -> return CameraFragment()
        }

    }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> return "CHATS"
            1 -> return "STATUS"
            else -> return "CALLS"
//            else -> return null
        }
    }
}