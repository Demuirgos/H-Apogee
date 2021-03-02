package UI

import adminSide.Request
import googleDrive.GoogleConn
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.Alert
import javafx.scene.text.FontWeight
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.mortbay.jetty.Main
import tornadofx.*

class RequestViewModel : ItemViewModel<Request>() {
    private val requestModel: RequestModel by inject()
    val requests = SimpleObjectProperty<ObservableList<Request>>()
    val selectedReq = bind {SimpleObjectProperty(item)}
    val selectedFirstName = bind { SimpleStringProperty(item?.firstName) }
    val selectedLastName = bind { SimpleStringProperty(item?.lastName) }
    val selectedEmail = bind {SimpleStringProperty(item?.email)}
    val selectedId = bind {SimpleStringProperty(item?.id)}
    val selectedCin = bind {SimpleStringProperty(item?.cin)}
    val selectedDate = bind {SimpleStringProperty(item?.date.toString())}
    val selectedType = bind {SimpleStringProperty(item?.docType.toString())}

    init {
        item = Request.empty()
    }

    fun reload() {
        runAsync {
            updateMessage("Chargement de requetes")
            requestModel.loadRequests()
            item = if(requestModel.requests.isEmpty()) Request.empty() else requestModel.requests[0]
        } ui {
            requests.set(requestModel.requests.toObservable())
        }
    }

    fun load(list: List<Request>) {
        requests.set(list.toObservable())
    }
}

class RequestModel: Controller() {
    val requests = mutableListOf<Request>()

    fun loadRequests() {
        requests.clear()
        requests.addAll(GoogleConn.checkFiles())
    }
}

class RequestListFragment : Fragment() {
    private val status: TaskStatus by inject()
    private val requestViewModel: RequestViewModel by inject()

    @ObsoleteCoroutinesApi
    override val root = vbox(4.0) {
        runAsync {
            runBlocking{
                val tickerEv = ticker(300000, 0)
                launch {
                    updateTitle("Consultation de Google Drive")
                    for (t in tickerEv)
                        reload()
                }
            }
        }

        hbox {
            button("Recharger") {
                setOnAction { reload() }
            }

            button("Supprimer") {
                setOnAction {
                    alert(Alert.AlertType.CONFIRMATION, "Voulez-vous supprimer cette requete?") {
                        requestViewModel.load(
                            GoogleConn.deleteFile(
                                requestViewModel.selectedReq.value.requestId,
                                requestViewModel.requests.value
                            )
                        )
                    }
                }
            }
        }

        listview<Request> {
            itemsProperty().bind(requestViewModel.requests)

            cellFormat {
                graphic = cache {
                    form {
                        fieldset {
                            field("Nom") {
                                label("${it.firstName} ${it.lastName}")
                            }

                            field("Date") {
                                label(it.date.toString())
                            }

                            label(it.docType.toString()) {
                                alignment = Pos.CENTER_RIGHT
                                style {
                                    fontSize = 16.px
                                    fontWeight = FontWeight.BOLD
                                }
                            }
                        }
                    }
                }
            }
            bindSelected(requestViewModel)
        }

        paddingAll = 5

        hbox(4.0) {
            progressbar(status.progress)
            label(status.message)
            visibleWhen { status.running }
            paddingAll = 4

        }
    }

    private fun reload() {
        requestViewModel.reload()
    }

}

class RequestDetailsFragment : Fragment() {
    private val requestViewModel: RequestViewModel by inject()

    override val root = vbox {
        hbox(5) {
            label(title) {
                text = "Détails de la requête:"
                style{
                    fontSize = 22.px
                    fontWeight = FontWeight.BOLD
                }
            }
        }

        hbox(5) {
            label("Prenom:")
            text(requestViewModel.selectedFirstName)
            style {
                fontSize=16.px
            }
        }

        hbox(5) {
            label("Nom:")
            text(requestViewModel.selectedLastName)
            style {
                fontSize=16.px
            }
        }

        hbox(5) {
            label("Email:")
            text(requestViewModel.selectedEmail)

            style {
                fontSize=16.px
            }
        }

        hbox(5) {
            label("Numero d'apogée:")
            text(requestViewModel.selectedId)

            style {
                fontSize=16.px
            }
        }

        hbox(5) {
            label("CIN:")
            text(requestViewModel.selectedCin)

            style {
                fontSize=16.px
            }
        }

        hbox(5) {
            label("Date:")

            text(requestViewModel.selectedDate)

            style {
                fontSize=16.px
            }
        }

        hbox(5) {
            label("Type Document:")

            text(requestViewModel.selectedType)

            style {
                fontSize=16.px
            }
        }
        paddingAll = 5.0

        style {
            fontSize=16.px
        }
    }
}

