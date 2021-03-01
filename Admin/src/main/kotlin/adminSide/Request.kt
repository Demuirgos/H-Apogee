package adminSide

import com.beust.klaxon.Klaxon
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class Request (
     val requestId: String
    , val firstName: String
    , val lastName: String
    , val email: String
    , val id: String
    , val cin: String
    , val date: LocalDate
    , val docType: Doc) {

    enum class Doc {
        ReleveDeNote,
        AttestationDeScolarite,
        AttestationDeStage,
        Default;

        companion object {
            fun parse(string: String) =
                when (string.toLowerCase()) {
                    "releve de note" -> ReleveDeNote
                    "attestation de scolarite" -> AttestationDeScolarite
                    "attestation de stage" -> AttestationDeStage
                    else -> Default
                }
        }

        override fun toString(): String =
            when (this) {
                ReleveDeNote -> "Relevé de notes"
                AttestationDeScolarite -> "Attestation de scolarité"
                AttestationDeStage -> "Attestation de stage"
                Default -> ""
            }
    }



    companion object {
        fun fromJson(jsonContent: String, requestId: String) =
            Klaxon().parse<Map<String, String>>(jsonContent)?.toRequest(requestId)

        private fun Map<String, String>.toRequest(requestId: String) =
            this["firstName"]?.let {
                val request = this["docType"]?.let { it1 -> Doc.parse(it1) }?.let { it2 ->
                    Request(
                        requestId,
                        it,
                        this["lastName"]!!,
                        this["email"]!!,
                        this["id"]!!,
                        this["cin"]!!,
                        LocalDate.parse(this["date"], DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                        it2
                    )
                }
                request
            }

        fun empty() =
            Request ("","", "", "", "", "", LocalDate.of(1970, 1, 1), Doc.Default)
    }

    override fun toString(): String =
        """Id Requete: $requestId
            |First name: $firstName
            |Last name: $lastName
            |Email: $email
            |id: $id
            |cin: $cin
            |date: $date
            |docType: $docType
        """.trimMargin()
}