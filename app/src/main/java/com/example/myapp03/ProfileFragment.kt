package com.example.myapp03

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: FirebaseDatabase
    lateinit var mDatabaseReference: DatabaseReference

    lateinit var tvName: TextView
    lateinit var tvEmail: TextView
    lateinit var tvNickname: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase.reference.child("MyUsers")
        val user = mAuth.currentUser

        val currentUserDb = mDatabaseReference.child(user!!.uid)
        currentUserDb.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var nickname = snapshot.child("nickname").value as String
                tvNickname.text = nickname
            }
        })

        tvName = view.findViewById<TextView>(R.id.textView)
        tvEmail = view.findViewById<TextView>(R.id.textView2)
        tvNickname = view.findViewById<Button>(R.id.textView3)

        tvName.text = user.uid
        tvEmail.text = user.email
    }

}
