package com.roc.projecte1

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {


    // Endpoints GET

//    @GET("/usuaris/{id}")
//    fun getUser(@Path("id") id: Int): Call<Alumne>

    @GET("/usuaris/{email}")
    fun getLogin(@Path("email") email: String): Call<LoginResponse>

    @GET("usuaris/classes/{id}")
    fun getClassesForUser(@Path("id") idUsuari: Int): Call<List<Classes>>

//    @GET("/producte/{id}")
//    fun getProduct(@Path("id") productId: Int): Call<Product>
//
//    @GET("/productes/")
//    fun getAllProducts(): Call<List<Product>>
//
//    @GET("/productes/{id}")
//    fun getProducts(@Path("id") sellerId: Int): Call<List<Product>>


//    // Enpoints POST
//
//    @POST("/nou_usuari/")
//    fun postUser(@Body user: Alumne): Call<Map<String, Any>>
//
//    @POST("/nou_producte/")
//    fun postProduct(@Body product: NewProduct): Call<Map<String, Any>>
//
//
//    // Endpoints PUT
//
//    @PUT("/editar_producte/{id}")
//    fun putProduct(@Path("id") id: Int, @Body product: Product): Call<Map<String, Any>>
//
//    @PUT("/editar_usuari/{id}")
//    fun putUser(@Path("id") id: Int, @Body user: Alumne): Call<Map<String, Any>>
//
//
//    //Endpoints DELETE
//
//    @DELETE("/eliminar_producte/{id}")
//    fun deleteProduct(@Path("id") id: Int): Call<Map<String, Any>>

}