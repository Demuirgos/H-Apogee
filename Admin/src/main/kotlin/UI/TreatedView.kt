package UI

import adminSide.TreatedRequest
import database.DatabaseApi
import javafx.application.Platform
import javafx.scene.Parent
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.paint.Color
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import tornadofx.*
import kotlin.system.exitProcess

class TreatedView : View("Requetes Traiter") {
    private val status: TaskStatus by inject()
    private val requestViewModel: TreatedRequestViewModel by inject()

    override val root: Parent =
        tableview(requestViewModel.requests) {
            readonlyColumn("#", TreatedRequest::requestId).minWidth(100)
            readonlyColumn("CIN", TreatedRequest::id).fixedWidth(100)
            readonlyColumn("CNE", TreatedRequest::cne).fixedWidth(100)
            readonlyColumn("Nom", TreatedRequest::lastName).fixedWidth(100)
            readonlyColumn("Prenom", TreatedRequest::firstName).fixedWidth(100)
            readonlyColumn("Temp", TreatedRequest::date).fixedWidth(100)


            readonlyColumn("Etat de requete", TreatedRequest::state) {
                text

            }

            smartResize()
            setMinSize(600.0, 300.0)
            setPrefSize(700.0, 400.0)

            bindSelected(requestViewModel)
        }

    fun exit() {
        alert(Alert.AlertType.CONFIRMATION, "Quitter?") {
            if(it == ButtonType.OK) {
                Platform.exit()
                exitProcess(0)
            }
            height = 50.0
            width = 100.0
        }
    }

    override fun onDelete() {
        alert(Alert.AlertType.CONFIRMATION, "Voulez-vous supprimer cette requete?") {
            if (it == ButtonType.OK) {
                val id =
                    requestViewModel.selectedReq.value.requestId
                runAsync {
                    transaction {
                        DatabaseApi.Requests.deleteWhere { DatabaseApi.Requests.idRequest eq id }
                    }
                }

                runAsync {
                    requestViewModel.load(
                        requestViewModel.requests.value.filter { it.requestId != id }
                    )
                }
            }
        }

    }

    override fun onDock() {
        super.onDock()
        requestViewModel.reload()
    }

    override fun onRefresh() {
        requestViewModel.reload()
    }
}