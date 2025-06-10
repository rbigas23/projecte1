package com.roc.projecte1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val sm by lazy { SessionManager.getInstance(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleTextView = view.findViewById<TextView>(R.id.fragment_profile_title)
        val editButton = view.findViewById<Button>(R.id.fragment_profile_edit_button)
        val logoffButton = view.findViewById<Button>(R.id.fragment_profile_logoff_button)

        val user = sm.getUser()

        val userName = when (user) {
            is Alumne -> user.nom.substringBefore(" ")
            is Professor -> user.nom.substringBefore(" ")
            else -> "Usuari"
        }

        titleTextView.text = "Hola, ${userName}!"

        editButton.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        logoffButton.setOnClickListener {
            logOff()
        }
    }

    private fun logOff() {
        sm.clearSession()
        startActivity(Intent(requireContext(), StartActivity::class.java))
        requireActivity().finish()
    }
}
