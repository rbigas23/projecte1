package com.roc.projecte1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class LogInActivity : AppCompatActivity() {

    private val apiService by lazy { RetrofitClient.apiService }
    private val sessionManager by lazy { SessionManager.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.activity_login_button).setOnClickListener {
            val inputEmail = findViewById<EditText>(R.id.activity_login_email).text.toString().trim()
            val inputPassword = findViewById<EditText>(R.id.activity_login_password).text.toString()

            when {
                inputEmail.isEmpty() -> showToast("Introdueix un email")
                !Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$").matches(inputEmail) ->
                    showToast("El camp 'Email' ha de tenir un format vàlid")
                inputPassword.isEmpty() -> showToast("Introdueix una contrasenya")
                else -> performLogin(inputEmail, inputPassword)
            }
        }
    }

    private fun performLogin(email: String, inputPassword: String) {
        apiService.getLogin(email).enqueue(object : Callback<LoginResponse> {

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    Log.d("LOGIN_RESPONSE", loginResponse.toString())

                    val userData = loginResponse?.userData
                    val userType = loginResponse?.userType

                    if (userData != null && userType != null) {
                        handleLoginResult(userData, userType, inputPassword )
                    } else {
                        Log.d("LOGIN", "Contrasenya incorrecte")
                        showToast("Credencials incorrectes")
                    }
                } else if (response.code() == 404) {
                    Log.d("LOGIN", "Usuari no trobat a la DB")
                    showToast("Credencials incorrectes")
                } else {
                    Log.e("API", "Error en la resposta HTTP: Código ${response.code()}")
                    showToast("Error de xarxa")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                if (t is HttpException && t.code() == 404) {
                    showToast("Credencials incorrectes")
                } else {
                    Log.e("API", "Error de xarxa: ${t.message}", t)
                    showToast("Error de xarxa")
                }
            }
        })
    }

    private fun handleLoginResult(userData: Map<String, Any>, userType: Char, inputPassword: String) {
        val storedPassword = userData["password"] as? String

        if (storedPassword == null) {
            showToast("Credencials incorrectes")
            return
        }

        if (storedPassword == inputPassword) {
            sessionManager.saveSession(userData, userType)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            showToast("Credencials incorrectes")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
