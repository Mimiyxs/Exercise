package ca.qc.apiexercise

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.Body


/*
* "userId": 1,
    "id": 2,
    "title": "qui est esse",
    "body*/

@Serializable
data class Post(
    @SerialName("userId") val idUtilisateur: Int,
    @SerialName("id") val idPost: Int,
    @SerialName("title") val titre: String,
    @SerialName("body") val body: String
)