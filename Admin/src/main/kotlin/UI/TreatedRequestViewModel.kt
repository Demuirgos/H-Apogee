package UI

import adminSide.RequestsModel
import adminSide.TreatedRequest
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import tornadofx.ItemViewModel
import tornadofx.toObservable

class TreatedRequestViewModel : ItemViewModel<TreatedRequest>() {
    private val requestModel: RequestsModel by inject()
    val requests = SimpleObjectProperty<ObservableList<TreatedRequest>>()
    val selectedReq = bind { SimpleObjectProperty(item) }

    fun reload() {
        runAsync {
            updateMessage("Chargement de requetes")
            requestModel.loadTreatedReqs()
        } ui {
            requests.set(requestModel.treatedRequests.toObservable())
        }
    }

    fun load(list: List<TreatedRequest>) {
        requests.set(list.toObservable())
    }
}
