package com.roc.projecte1

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.roc.projecte1.RetrofitClient.apiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.orEmpty

class AssistenciesFragment : Fragment(R.layout.fragment_assist) {

    private val sm by lazy { SessionManager.getInstance(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val assistsRecyclerView:RecyclerView = view.findViewById(R.id.assists_recycler_view)

        val user = sm.getUser()

        val userId = when (user) {
            is Alumne -> user.idAlumne ?: -1
            is Professor -> user.idProfessor ?: -1
            else -> -1
        }

        apiService.getAssistenciesForUser(userId)
            .enqueue(object : Callback<List<Assistencia>> {
                override fun onResponse(call: Call<List<Assistencia>>, resp: Response<List<Assistencia>>) {
                    if (resp.isSuccessful) {
                        val assistsData = resp.body().orEmpty()
                        Log.d("ASSISTENCIES", "Data: $assistsData")
                        setupRecyclerView(assistsRecyclerView, assistsData)
                    } else {
                        Log.e("ASSISTENCIES", "HTTP Error: ${resp.code()}, ${resp.errorBody()?.string()}")
                        Toast.makeText(context, "Error HTTP ${resp.code()}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<List<Assistencia>>, t: Throwable) {
                    Toast.makeText(context, "Error de xarxa", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, data: List<Assistencia>) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = AssistenciesAdapter(data)
    }

}
