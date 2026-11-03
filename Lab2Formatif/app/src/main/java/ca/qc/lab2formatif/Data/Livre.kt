package ca.qc.lab2formatif.Data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Livre(
    @SerialName("title")
    val titre: String = "",
    @SerialName("author_name")
    val auteurs: List<String>? = null,
    @SerialName("first_publish_year")
    val anneePublication : Int? = null
)
