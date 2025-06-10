package com.roc.projecte1

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.roc.projecte1.RetrofitClient.apiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val sm by lazy { SessionManager.getInstance(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val greetingText: TextView = view.findViewById(R.id.greeting_text)
        val classesRecyclerView: RecyclerView = view.findViewById(R.id.classes_recycler_view)
        val assistsRecyclerView: RecyclerView = view.findViewById(R.id.home_assists_recycler_view)

        val user = sm.getUser()

        val userName = when (user) {
            is Alumne -> user.nom.substringBefore(" ")
            is Professor -> user.nom.substringBefore(" ")
            else -> "Usuari"
        }

        val userId = when (user) {
            is Alumne -> user.idAlumne ?: -1
            is Professor -> user.idProfessor ?: -1
            else -> -1
        }

        greetingText.text = "Benvingut/da, $userName!"

        setupContent(classesRecyclerView, assistsRecyclerView, userId)
    }

    private fun setupContent(
        classesRecyclerView: RecyclerView,
        assistsRecyclerView: RecyclerView,
        userId: Int
    ) {
        classesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        classesRecyclerView.adapter = ClassesAdapter(emptyList())

        Log.d("USER_ID", userId.toString())

        apiService.getClassesForUser(userId)
            .enqueue(object : Callback<List<Classes>> {
                override fun onResponse(call: Call<List<Classes>>, resp: Response<List<Classes>>) {
                    Log.d("CLASSES", "onResponse called")

                    if (resp.isSuccessful) {
                        val classesData = resp.body().orEmpty()
                        Log.d("CLASSES", "Classes Data: $classesData")
                        setupRecyclerView(classesRecyclerView, classesData)
                    } else {
                        Log.e("CLASSES", "HTTP Error: ${resp.code()}, ${resp.errorBody()?.string()}")
                        Toast.makeText(context, "Error HTTP ${resp.code()}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<List<Classes>>, t: Throwable) {
                    Toast.makeText(context, "Error de xarxa", Toast.LENGTH_SHORT).show()
                }
            })

        apiService.getAssistencies7DaysForUser(userId)
            .enqueue(object : Callback<List<Assistencia>> {
                override fun onResponse(call: Call<List<Assistencia>>, resp: Response<List<Assistencia>>) {
                    if (resp.isSuccessful) {
                        val assistsData = resp.body().orEmpty()
                        Log.d("ASSISTENCIES_HOME", "Data: $assistsData")
                        setupRecyclerView(assistsRecyclerView, assistsData)
                    } else {
                        Log.e("ASSISTENCIES_HOME", "HTTP Error: ${resp.code()}, ${resp.errorBody()?.string()}")
                        Toast.makeText(context, "Error HTTP ${resp.code()}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<List<Assistencia>>, t: Throwable) {
                    Toast.makeText(context, "Error de xarxa", Toast.LENGTH_SHORT).show()
                }
            })

    }


    private fun setupRecyclerView(recyclerView: RecyclerView, data: List<Any>) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        when {
            data.isNotEmpty() && data.first() is Classes -> {
                recyclerView.adapter = ClassesAdapter(data as List<Classes>)
            }
            data.isNotEmpty() && data.first() is Assistencia -> {
                recyclerView.adapter = AssistenciesAdapter(data as List<Assistencia>)
            }
            else -> {
                recyclerView.adapter = null
            }
        }
    }

}
