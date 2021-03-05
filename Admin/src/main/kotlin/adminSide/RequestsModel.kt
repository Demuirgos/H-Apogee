package adminSide

import googleDrive.GoogleApiDriver
import tornadofx.Controller

class RequestsModel: Controller() {
    val requests = mutableListOf<Request>()

    fun loadRequests() {
        requests.clear()
        requests.addAll(GoogleApiDriver.checkFiles())
    }
}



