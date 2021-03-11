package UI

import adminSide.Request
import googleDrive.GoogleApiDriver
import javafx.geometry.Pos
import javafx.scene.control.Alert
import javafx.scene.text.FontWeight
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import tornadofx.*

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



        listview(requestViewModel.requests){
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

    fun delete() {
        alert(Alert.AlertType.CONFIRMATION, "Voulez-vous supprimer cette requete?") {
            val id =
                requestViewModel.selectedReq.value.requestId
            runAsync {
                GoogleApiDriver.deleteFile(id)
                ui {
                    requestViewModel.reload()
                }
            }
        }
    }

    fun remove(rId: String) {
        requestViewModel.load(requestViewModel.requests.value.filter { it.requestId != rId })
    }

    fun reload() {
        requestViewModel.reload()
    }

    fun load() {
        requestViewModel.newLoad()
    }
}
