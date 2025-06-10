package com.roc.projecte1;

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {

    private var currentStep = 0
    var tipusUsuari: Char = '0'

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        loadStep(currentStep)
    }

    private fun loadStep(step: Int) {
        val container = findViewById<FrameLayout>(R.id.container)
        container.removeAllViews()

        when (step) {
            0 -> loadStep1(container)
            1 -> loadStep2(container)
        }
    }

    private fun loadStep1(container: FrameLayout) {
        val view = layoutInflater.inflate(R.layout.register_type, container, false)

        val checkBoxA = view.findViewById<CheckBox>(R.id.register_type_check_box_a)
        val checkBoxP = view.findViewById<CheckBox>(R.id.register_check_box_p)
        val nextButton = view.findViewById<ImageButton>(R.id.register_type_next_button)

        checkBoxA.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkBoxP.isChecked = false
                tipusUsuari = 'a'
            }
        }

        checkBoxP.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkBoxA.isChecked = false
                tipusUsuari = 'p'
            }
        }

        nextButton.setOnClickListener {
            if (tipusUsuari != 'a' && tipusUsuari != 'p') {
                Toast.makeText(this, "Rol no seleccionat", Toast.LENGTH_SHORT).show()
            } else {
                currentStep++
                loadStep(currentStep)
            }
        }

        container.addView(view)
    }

    private fun loadStep2(container: FrameLayout) {
        val view = layoutInflater.inflate(R.layout.register_data, container, false)

        val dataNaixementEditText = view.findViewById<EditText>(R.id.register_data_naixement)

        dataNaixementEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                    dataNaixementEditText.setText(selectedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        val nomEditText = view.findViewById<EditText>(R.id.register_data_name)
        val emailEditText = view.findViewById<EditText>(R.id.register_data_email)
        val contrasenyaEditText = view.findViewById<EditText>(R.id.register_data_password)
        val confirmarContrasenyaEditText =
            view.findViewById<EditText>(R.id.register_data_password_confirmation)


        val submitButton = view.findViewById<Button>(R.id.register_data_submit_button)

        submitButton.setOnClickListener {

            val nom = nomEditText.text.toString()
            val email = emailEditText.text.toString()
            val contrasenya = contrasenyaEditText.text.toString()
            val confirmarcontrasenya = confirmarContrasenyaEditText.text.toString()
            val dataNaixement = dataNaixementEditText.text.toString()

            when {

                nom.isEmpty() -> Toast.makeText(
                    this@RegisterActivity,
                    "El camp 'Nom' no pot estar buit",
                    Toast.LENGTH_SHORT
                ).show()


                email.isEmpty() -> Toast.makeText(
                    this@RegisterActivity,
                    "El camp 'Email' no pot estar buit",
                    Toast.LENGTH_SHORT
                ).show()

                !Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$").matches(email) -> Toast.makeText(
                    this@RegisterActivity,
                    "El camp 'Email' ha de tenir un format vàlid",
                    Toast.LENGTH_SHORT
                ).show()


                contrasenya.isEmpty() -> Toast.makeText(
                    this@RegisterActivity,
                    "El camp 'Contrasenya' no pot estar buit",
                    Toast.LENGTH_SHORT
                ).show()

                !Regex("^(?=.*[A-Z])(?=.*[!@#\$%^&*(),.?\":{}|<>]).{6,}$").matches(contrasenya) -> Toast.makeText(
                    this@RegisterActivity,
                    "El camp 'Contrasenya' ha de tenir almenys 6 caràcters, una majúscula i un caràcter especial",
                    Toast.LENGTH_SHORT
                ).show()


                confirmarcontrasenya.isEmpty() -> Toast.makeText(
                    this@RegisterActivity,
                    "Siusplau, confirma la contrasenya",
                    Toast.LENGTH_SHORT
                ).show()

                !confirmarcontrasenya.equals(contrasenya) -> Toast.makeText(
                    this@RegisterActivity,
                    "Les contrasenyes no coincideixen",
                    Toast.LENGTH_SHORT
                ).show()


                dataNaixement.isEmpty() -> Toast.makeText(
                    this@RegisterActivity,
                    "La data de naixement és obligatòria",
                    Toast.LENGTH_SHORT
                ).show()

                else -> {

                    val user = if (tipusUsuari == 'a') {
                        Alumne(
                            nom = nom,
                            email = email,
                            password = contrasenya,
                            dataNaixement = dataNaixementEditText.text.toString(),
                        )
                    } else {
                        Professor(
                            nom = nom,
                            email = email,
                            password = contrasenya,
                            dataNaixement = dataNaixementEditText.text.toString(),
                        )
                    }

                    finishRegistration(user)

                }
            }

        }
        container.addView(view)
    }

    private fun finishRegistration(user: Any) {
        val call = when (user) {
            is Professor -> RetrofitClient.apiService.postProfessor(user)
            is Alumne -> RetrofitClient.apiService.postAlumne(user)
            else -> {
                Toast.makeText(this, "Error: tipus d'usuari no vàlid", Toast.LENGTH_SHORT).show()
                return
            }
        }

        call.enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(
                call: Call<Map<String, Any>>,
                response: Response<Map<String, Any>>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registre completat amb èxit",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@RegisterActivity, LogInActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("API", "Error: ${response.errorBody()?.string()}")
                    Toast.makeText(
                        this@RegisterActivity,
                        "Error en el registre",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                Log.e("API", "Error de connexió: ${t.message}")
                Toast.makeText(this@RegisterActivity, "Error de connexió", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

}
