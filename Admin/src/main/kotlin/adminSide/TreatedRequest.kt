package adminSide

import java.time.LocalDate

class TreatedRequest(
    val requestId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val id: Int,
    val cne: String,
    val cin: String,
    val date: LocalDate,
    val docType: Doc,
    val state: StateReq)  {

    enum class StateReq {
        Accepter, Refuser;

        override fun toString(): String =
           when (this) {
               Accepter -> "Accepter"
               Refuser -> "Refuser"
           }
    }

    companion object {
        fun fromRequest(req: Request, state: Boolean) =
            TreatedRequest(req.requestId, req.firstName, req.lastName, req.email, req.id, req.cne, req.cin, req.date, req.docType, if (state) StateReq.Accepter else StateReq.Refuser)

        fun empty() =
            fromRequest(Request.empty(), false)
    }

    fun toCsv() =
        "$requestId, $firstName, $lastName, $email, $id, $cne, $cin, $date, $docType, $state"
}
