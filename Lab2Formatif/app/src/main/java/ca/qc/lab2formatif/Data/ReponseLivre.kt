package ca.qc.lab2formatif.Data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReponseLivre(
    @SerialName("docs")
    val livres: List<Livre> = emptyList()
)