package ca.qc.livremt

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/*
*
"docs": [
{
"title": "The Hobbit",
"author_name": ["J.R.R. Tolkien"],
"first_publish_year": 1937
}
]
* */

@Serializable
data class Doc(
    @SerialName("title") val titre: String,
    @SerialName("author_name") val nom_auteur: String,
    @SerialName("first_publish_year") val anneePublication: Int,
)

data class ReponseLivre(
    @SerialName("docs")
    val livres: List<Livre>
)

data class Livre(
    @SerialName("title")
    val titre: String,
    @SerialName("author_name")
    val auteurs: List<String>,
    @SerialName("first_publish_year")
    val anneePublication: Int
)