package adminSide

import database.DatabaseApi
import googleDrive.GoogleApiDriver
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import tornadofx.Controller
import utils.Either

class RequestsModel: Controller() {
    val requests = mutableListOf<Request>()
    val treatedRequests = mutableListOf<Request>()

    fun loadRequests() {
        requests.clear()
        requests.addAll(GoogleApiDriver.checkFiles())

    }

    fun loadTreatedReqs() {
        treatedRequests.clear()
        treatedRequests.addAll(transaction {
            return@transaction DatabaseApi.Requests.selectAll().map {
                Request.fromJson(
                    it[Expression.build { DatabaseApi.Requests.fichierReq }],
                    it[Expression.build { DatabaseApi.Requests.idRequest }]
                )
            }.map {
                when (it) {
                    is Either.Right -> it.value!!
                    else -> Request.empty()
                }
            }.filter {
                it != Request.empty()
            }
        })
    }
}



