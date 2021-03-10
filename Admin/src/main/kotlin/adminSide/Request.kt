package adminSide

import com.beust.klaxon.Klaxon
import database.DatabaseApi
import googleDrive.GoogleApiDriver
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import utils.Either
import java.time.LocalDate
import java.time.format.DateTimeFormatter

open class Request (
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
            Klaxon().parse<Map<String, Any>>(jsonContent)?.mapValues { it.value.toString() }
                ?.toRequest(requestId)

        private fun Map<String, String>.toRequest(requestId: String)=
            if (this.keys.containsAll("FirstName Request LastName Email Id CIN CNE Date".split(" "))) {
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
                                LocalDate.parse(this["Date"], DateTimeFormatter.ofPattern("dd MM yyyy")),
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
            Request ("0","null", "null", "null", 0,"0", "0", LocalDate.of(1970, 1, 1), Doc.Default)
    }

    fun addToDB(b: Boolean) {
        transaction {
            DatabaseApi.Requests.insert {
                it[idRequest] = this@Request.requestId
                it[studentId] = this@Request.id
                it[fichierReq] = GoogleApiDriver.downloadFile(this@Request.requestId)
                it[etatRequest] = b
            }
        }
        GoogleApiDriver.deleteFile(this.requestId)
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