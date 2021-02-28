package adminSide

import com.beust.klaxon.Klaxon
import java.io.File

class Request (
    val firstName: String
    , val lastName: String
    , val email: String
    , val id: String
    , val cin: String
    , val date: String
    , val docType: String) {

    enum class Doc (docType: String){
        ReleveDeNote(""),
        AttestationDeScolarite("")
    }

    companion object {
        fun fromJson(fileName: String) =
            Klaxon().parse<Request>(File(fileName).readText())

        fun Empty() =
            Request ("", "", "", "", "", "", "")
    }

    override fun toString(): String =
        """First name: $firstName
            |Last name: $lastName
            |Email: $email
            |id: $id
            |cin: $cin
            |date: $date
            |docType: $docType
        """.trimMargin()
}