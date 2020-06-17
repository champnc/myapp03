package com.example.myapp03

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

/**
 * A simple [Fragment] subclass.
 */
class WelcomeFragment : Fragment() {

    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        val loginButton = view.findViewById<Button>(R.id.loginBtn)
        loginButton.setOnClickListener {
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToAuthenticationFragment()
            action.isLogin = true
            navController.navigate(action)
        }

        val tvRegister = view.findViewById<TextView>(R.id.tvRegister)
        tvRegister.setOnClickListener {
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToAuthenticationFragment()
            action.isLogin = false
            navController.navigate(action)
        }
    }

}