package com.erenalparslan.octopusclone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.erenalparslan.octopusclone.databinding.FragmentLoginBinding


class Login : Fragment() {

      private var fragmentLoginBinding: FragmentLoginBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val binding=FragmentLoginBinding.bind(view)
        fragmentLoginBinding=binding

        binding.button.setOnClickListener {
            println("press")
            findNavController().navigate(
            LoginDirections.actionLoginToPlay()
            )
        }

        super.onViewCreated(view, savedInstanceState)
    }


}