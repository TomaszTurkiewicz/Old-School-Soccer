package com.tt.oldschoolsoccer.fragments

import android.graphics.Shader
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.Functions
import com.tt.oldschoolsoccer.drawable.TileDrawable
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : Fragment() {

    var screenUnit:Int=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenUnit= Functions.readScreenUnit(requireContext())

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_main,container,false)

        rootView.fragment_one_layout.background = TileDrawable((ContextCompat.getDrawable(requireContext(),R.drawable.background)!!),
        Shader.TileMode.REPEAT,screenUnit)

        return rootView
    }




}