package com.roc.projecte1

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val sm by lazy { SessionManager.getInstance(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (sm.getUserType()) {
            "a" -> showAlumneContent(view)
            "p" -> showProfesorContent(view)
            else -> startActivity(Intent(this.context, StartActivity::class.java))
        }
    }

    private fun showAlumneContent(v: View) {
        // p.ej.: v.findViewById<TextView>(R.id.title).text = "Tu Horario"
    }

    private fun showProfesorContent(v: View) {
        // p.ej.: v.findViewById<TextView>(R.id.title).text = "Tus Asignaturas"
    }
}
