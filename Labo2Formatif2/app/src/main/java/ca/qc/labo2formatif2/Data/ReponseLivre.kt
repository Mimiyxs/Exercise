package ca.qc.labo2formatif2.Data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReponseLivre(
    @SerialName("docs")
    val livres: List<Livre>
)