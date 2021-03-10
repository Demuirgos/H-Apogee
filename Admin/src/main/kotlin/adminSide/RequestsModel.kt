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
    val treatedRequests = mutableListOf<TreatedRequest>()

    fun loadRequests() {
        requests.clear()
        requests.addAll(GoogleApiDriver.checkFiles())
    }

    fun load() {
        requests.clear()
        requests.addAll(GoogleApiDriver.checkFiles())
    }

    fun loadTreatedReqs() {
        treatedRequests.clear()
        treatedRequests.addAll(DatabaseApi.getReqs())
    }
}



