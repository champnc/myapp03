package com.example.myapp03

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.myapp03.models.User
import com.example.myapp03.util.getLoading
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * A simple [Fragment] subclass.
 */
class AuthenticationFragment : Fragment() {
    lateinit var navController: NavController

    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: FirebaseDatabase
    lateinit var mDatabaseReference: DatabaseReference

    lateinit var tvSwap: TextView
    lateinit var tvTitle: TextView
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var etNickNameInput: EditText
    lateinit var loginBtn: Button
    lateinit var etNickName: TextInputLayout
    var isLoginForm: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_authentication, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvSwap = view.findViewById<TextView>(R.id.tvSwap)
        tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        loginBtn = view.findViewById<Button>(R.id.loginBtn)

        etEmail = view.findViewById<EditText>(R.id.etEmailInput)
        etPassword = view.findViewById<EditText>(R.id.etPasswordInput)
        etNickNameInput = view.findViewById<EditText>(R.id.etNickNameInput)
        etNickName = view.findViewById<TextInputLayout>(R.id.etNickName)

        tvSwap.setOnClickListener {
            isLoginForm = !isLoginForm
            swapForm()
        }

        loginBtn.setOnClickListener {
            val email = etEmail.text.toString().trim { it <= ' ' }
            val password = etPassword.text.toString().trim { it <= ' ' }
            val nickname = etNickNameInput.text.toString().trim { it <= ' ' }

            if (email.isEmpty()) {
                Toast.makeText(activity, "Please enter your email address.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(activity, "Please enter your password.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isLoginForm) {
                login(email, password)
            } else {
                register(email, password, nickname)
            }
        }

        arguments?.let { arguments ->
            val args = AuthenticationFragmentArgs.fromBundle(arguments)
            isLoginForm = args.isLogin
            swapForm()
        }

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("MyUsers")
    }

    fun swapForm() {
        if (isLoginForm) {
            tvSwap.text = "Register"
            tvTitle.text = "Login"
            loginBtn.text = "Login"
            etNickName.visibility =  View.GONE
        } else {
            tvSwap.text = "Login"
            tvTitle.text = "Register"
            loginBtn.text = "Register"
            etNickName.visibility =  View.VISIBLE
        }
    }

    private fun register(email: String, password: String, nickname: String) {
        var dialog = getLoading()
        dialog.show()
        mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            dialog.dismiss()
            if (!task.isSuccessful) {
                if (password.length < 6) {
                    Toast.makeText(activity, "Password too short! Please enter minimum 6 characters.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "Authentication Failed: " + task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            } else {
                // Case Success
                mAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener{
                    Toast.makeText(activity, "Create account successfully!, Please check your email for verification", Toast.LENGTH_SHORT).show()
                }
                // Update Nickname field
                val userId = mAuth!!.currentUser!!.uid
                val currentUserDb = mDatabaseReference!!.child(userId)
                currentUserDb.setValue(User(email, nickname)).addOnCompleteListener{ task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(activity, "Update Failed: " + task.exception!!.message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity, "Update Done", Toast.LENGTH_SHORT).show()
                        // Go to Profile Screen
                    }
                }
            }
        }
    }

    private fun login(email: String, password: String) {
        var dialog = getLoading()
        dialog.show()
        mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            dialog.dismiss()
            if (!task.isSuccessful) {
                if (password.length < 6) {
                    Toast.makeText(activity, "Please check your password. Password must have minimum 6 characters.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "Authentication Failed: " + task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(activity, "Sign in successfully!", Toast.LENGTH_SHORT).show()
                var action = AuthenticationFragmentDirections.actionAuthenticationFragmentToProfileFragment()
                findNavController().navigate(action)

            }
        }
    }

}