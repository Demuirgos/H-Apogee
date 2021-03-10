package UI

import adminSide.Request
import adminSide.RequestsModel
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import tornadofx.ItemViewModel
import tornadofx.toObservable

class RequestViewModel : ItemViewModel<Request>() {
    private val requestModel: RequestsModel by inject()
    val requests = SimpleObjectProperty<ObservableList<Request>>()
    val selectedReq = bind { SimpleObjectProperty(item) }
    val selectedFirstName = bind { SimpleStringProperty(item?.firstName) }
    val selectedLastName = bind { SimpleStringProperty(item?.lastName) }
    val selectedEmail = bind { SimpleStringProperty(item?.email) }
    val selectedId = bind {SimpleStringProperty(item?.id.toString())}
    val selectedCne = bind { SimpleStringProperty(item?.cne) }
    val selectedCin = bind { SimpleStringProperty(item?.cin) }
    val selectedDate = bind { SimpleStringProperty(item?.date.toString()) }
    val selectedType = bind { SimpleStringProperty(item?.docType.toString()) }

    init {
        item = Request.empty()
    }

    fun reload() {
        runAsync {
            updateMessage("Chargement de requetes")
            requestModel.loadRequests()
            item = if (requestModel.requests.isEmpty()) Request.empty() else requestModel.requests[0]
            ui {
                requests.set(requestModel.requests.distinctBy { it.requestId }.toObservable())
            }
        }
    }

    fun load(list: List<Request>) {
        requests.set(list.toObservable())
    }

    fun newLoad() {
        runAsync {
            updateMessage("Chargement de requetes")
            requestModel.load()
            item = if(requestModel.requests.isEmpty()) Request.empty() else requestModel.requests[0]
        } ui {
            requests.set(requestModel.requests.distinctBy { it.requestId }.toObservable())
        }

    }
}
