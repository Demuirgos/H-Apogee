package adminSide

import com.beust.klaxon.Klaxon
import utils.Either
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Request (
     val requestId: String
    , val firstName: String
    , val lastName: String
    , val email: String
    , val id: Int
    , val cne: String
    , val cin: String
    , val date: LocalDate
    , val docType: Doc) {


    companion object {
        fun fromJson(jsonContent: String, requestId: String) =
            Klaxon().parse<Map<String, String>>(jsonContent)?.toRequest(requestId)

        private fun Map<String, String>.toRequest(requestId: String)=
            if (this.keys.containsAll("FirstName Request LastName Email Id CIN Date".split(" "))) {
                Either.Right(
                    this["FirstName"]?.let {
                        val request = this["Request"]?.let { it1 -> Doc.parse(it1) }?.let { it2 ->
                            Request(
                                requestId,
                                it,
                                this["LastName"]!!,
                                this["Email"]!!,
                                this["Id"]!!.toInt(),
                                this["CNE"]!!,
                                this["CIN"]!!,
                                LocalDate.parse(this["Date"], DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                                it2
                            )
                        }
                        request
                    }
                )
            } else {
                Either.Left(Error("Wrong fields available"))
            }

        fun empty() =
            Request ("0","null", "null", "null", 0, "0", "0", LocalDate.of(1970, 1, 1), Doc.Default)
    }

    override fun toString(): String =
        """Id Requete: $requestId
            |First name: $firstName
            |Last name: $lastName
            |Email: $email
            |id: $cne
            |cin: $cin
            |date: $date
            |docType: $docType
        """.trimMargin()
}