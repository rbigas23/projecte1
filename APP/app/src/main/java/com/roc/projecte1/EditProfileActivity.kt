package com.roc.projecte1

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {

    private val sm by lazy { SessionManager.getInstance(this@EditProfileActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val fieldMap = mutableMapOf<String, EditText>()

        fieldMap["name"] = findViewById(R.id.activity_edit_profile_name)
        fieldMap["email"] = findViewById(R.id.activity_edit_profile_email)
        fieldMap["password"] = findViewById(R.id.activity_edit_profile_password)
        fieldMap["password2"] = findViewById(R.id.activity_edit_profile_password_confirmation)

        val saveButton: Button = findViewById(R.id.activity_edit_profile_save_button)

        val smUser = sm.getUser()

        val (userId, userType) = when (smUser) {
            is Alumne -> smUser.idAlumne to 'a'
            is Professor -> smUser.idProfessor to 'p'
            else -> -1 to ' '
        }

        // Obtenim les dades ja existents de l'usuari
        getUser(userId!!, userType) { user ->
            when (userType) {
                'a' -> {
                    val alumne = user as Alumne
                    loadUserData(alumne, fieldMap)
                    Log.d("API", "Alumne: ${alumne.nom}")
                }

                'p' -> {
                    val professor = user as Professor
                    loadUserData(professor, fieldMap)
                    Log.d("API", "Professor: ${professor.nom}")
                }
            }
        }

        // Al fer clic en el botó de guardar processem i validem les dades intrduïdes i
        // persistim l'usuari modificad a la base de dades
        saveButton.setOnClickListener {

            // Obtenim els valors introduïts per l'usuari
            val name = fieldMap["name"]?.text.toString().trim()
            val email = fieldMap["email"]?.text.toString().trim()
            val password = fieldMap["password"]?.text.toString().trim()
            val password2 = fieldMap["password2"]?.text.toString().trim()


            // Identifiquem si hi ha algun valor erroni i mostrem el missatge corresponent
            when {

                name.isEmpty() -> Toast.makeText(
                    this@EditProfileActivity,
                    "El camp 'Nom' no pot estar buit.",
                    Toast.LENGTH_SHORT
                ).show()


                email.isEmpty() -> Toast.makeText(
                    this@EditProfileActivity,
                    "El camp 'Email' no pot estar buit.",
                    Toast.LENGTH_SHORT
                ).show()

                !Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$").matches(email) -> Toast.makeText(
                    this@EditProfileActivity,
                    "El camp 'Email' ha de tenir un format vàlid",
                    Toast.LENGTH_SHORT
                ).show()


                !password.isEmpty() && !Regex("^(?=.*[A-Z])(?=.*[!@#\$%^&*(),.?\":{}|<>]).{6,}$").matches(
                    password
                ) -> Toast.makeText(
                    this@EditProfileActivity,
                    "El camp 'Contrasenya' ha de tenir almenys 6 caràcters, una majúscula i un caràcter especial",
                    Toast.LENGTH_SHORT
                ).show()


                !password.isEmpty() && password2.isEmpty() -> Toast.makeText(
                    this@EditProfileActivity,
                    "Siusplau, confirma la contrasenya",
                    Toast.LENGTH_SHORT
                ).show()

                !password.isEmpty() && !password2.equals(password) -> Toast.makeText(
                    this@EditProfileActivity,
                    "Les contrasenyes no coincideixen",
                    Toast.LENGTH_SHORT
                ).show()


                else -> {
                    // Persistim els canvis a la base de dades
                    val userData = UserUpdateDto(nom = name, email = email, password = password)
                    updateUser(userId, userType, userData)
                }
            }
        }

    }


    // Retorna un objecte Alumne o Professor segons la id de l'usuari actual
    private fun getUser(userId: Int, userType: Char, callback: (Any?) -> Unit) {
        when (userType) {
            'a' -> {
                RetrofitClient.apiService.getAlumne(userId).enqueue(object : Callback<Alumne> {
                    override fun onResponse(call: Call<Alumne>, response: Response<Alumne>) {
                        if (response.isSuccessful) {
                            callback(response.body())
                        } else {
                            Log.e(
                                "API",
                                "Error al obtenir alumne: ${response.errorBody()?.string()}"
                            )
                            callback(null)
                        }
                    }

                    override fun onFailure(call: Call<Alumne>, t: Throwable) {
                        Log.e("API", "Error obtenint alumne: ${t.message}")
                        callback(null)
                    }
                })
            }

            'p' -> {
                RetrofitClient.apiService.getProfessor(userId)
                    .enqueue(object : Callback<Professor> {
                        override fun onResponse(
                            call: Call<Professor>,
                            response: Response<Professor>
                        ) {
                            if (response.isSuccessful) {
                                callback(response.body())
                            } else {
                                Log.e(
                                    "API",
                                    "Error al obtenir professor: ${response.errorBody()?.string()}"
                                )
                                callback(null)
                            }
                        }

                        override fun onFailure(call: Call<Professor>, t: Throwable) {
                            Log.e("API", "Error obteninr professor: ${t.message}")
                            callback(null)
                        }
                    })
            }

            else -> {
                Log.e("API", "Tipus d'usuari desconegut")
                callback(null)
            }
        }
    }


    // Carrega les dades d'un Alumne o un Professor als diferents camps de la vista
    private fun loadUserData(
        user: Any,
        fieldMap: Map<String, EditText>
    ) {
        when (user) {
            is Alumne -> {
                fieldMap["name"]?.setText(user.nom)
                fieldMap["email"]?.setText(user.email)
            }

            is Professor -> {
                fieldMap["name"]?.setText(user.nom)
                fieldMap["email"]?.setText(user.email)
            }
        }

    }


    // Consumeix l'enpoint de modificació d'usuari de l'API i gestiona possibles errors
    private fun updateUser(userId: Int, userType: Char, updatedData: UserUpdateDto) {
        when (userType) {
            'a' -> { // Alumne
                RetrofitClient.apiService.updateAlumne(userId, updatedData)
                    .enqueue(object : Callback<UserUpdateDto> {
                        override fun onResponse(
                            call: Call<UserUpdateDto>,
                            response: Response<UserUpdateDto>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    this@EditProfileActivity,
                                    "Dades actualitzades correctament",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            } else {
                                Log.e(
                                    "API",
                                    "Error al actualitzar alumne: ${response.errorBody()?.string()}"
                                )
                                Toast.makeText(
                                    this@EditProfileActivity,
                                    "Error al actualitzar el perfil",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<UserUpdateDto>, t: Throwable) {
                            Log.e("API", "Error de connexió actualitzant alumne: ${t.message}")
                            Toast.makeText(
                                this@EditProfileActivity,
                                "Error de connexió",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }

            'p' -> { // Professor
                RetrofitClient.apiService.updateProfessor(userId, updatedData)
                    .enqueue(object : Callback<UserUpdateDto> {
                        override fun onResponse(
                            call: Call<UserUpdateDto>,
                            response: Response<UserUpdateDto>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    this@EditProfileActivity,
                                    "Dades actualitzades correctament",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            } else {
                                Log.e(
                                    "API",
                                    "Error al actualitzar professor: ${
                                        response.errorBody()?.string()
                                    }"
                                )
                                Toast.makeText(
                                    this@EditProfileActivity,
                                    "Error al actualitzar el perfil",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<UserUpdateDto>, t: Throwable) {
                            Log.e("API", "Error de connexió actualitzant professor: ${t.message}")
                            Toast.makeText(
                                this@EditProfileActivity,
                                "Error de connexió",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
        }
    }

}
