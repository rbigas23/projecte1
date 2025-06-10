package com.roc.projecte1

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {


    // Endpoints GET

    @GET("/alumnes/{id}")
    fun getAlumne(@Path("id") alumneId: Int): Call<Alumne>

    @GET("/professors/{id}")
    fun getProfessor(@Path("id") professorId: Int): Call<Professor>

    @GET("/usuaris/{email}")
    fun getLogin(@Path("email") email: String): Call<LoginResponse>

    @GET("usuaris/classes/{id}")
    fun getClassesForUser(@Path("id") idUsuari: Int): Call<List<Classes>>

    @GET("usuaris/assistencies/{id_usuari}")
    fun getAssistenciesForUser(@Path("id_usuari") userId: Int): Call<List<Assistencia>>

    @GET("usuaris/assistencies_home/{id_usuari}")
    fun getAssistencies7DaysForUser(@Path("id_usuari") userId: Int): Call<List<Assistencia>>


    // Enpoints POST

    @POST("/alumnes/")
    fun postAlumne(@Body alumne: Alumne): Call<Map<String, Any>>

    @POST("/professors/")
    fun postProfessor(@Body professor: Professor): Call<Map<String, Any>>


    // Endpoints PUT

    @PUT("alumnes/{id_alumne}")
    fun updateAlumne(
        @Path("id_alumne") idAlumne: Int,
        @Body alumneUpdate: UserUpdateDto
    ): Call<UserUpdateDto>

    @PUT("professors/{id_professor}")
    fun updateProfessor(
        @Path("id_professor") idProfessor: Int,
        @Body professorData: UserUpdateDto
    ): Call<UserUpdateDto>

}