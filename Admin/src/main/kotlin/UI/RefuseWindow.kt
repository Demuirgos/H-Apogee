package UI

import database.LogSession
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.Parent
import tornadofx.*

class RefuseWindow : View("RefuseWindow") {
    private val model: ViewModel = ViewModel()
    private val requestView: RequestViewModel by inject()
    private val reasonField= model.bind { SimpleStringProperty() }

    override val root: Parent =
        form {
            fieldset ("Raison du refus:") {
                textarea(reasonField).required()

                minHeight = 200.0
                minWidth = 300.0
            }

            hbox(5) {
                button("Confirmer").action {
                    runAsync {
                        LogSession.get().approve(requestView.selectedReq.value, reasonField.value, false)
                        ui {
                            RequestListFragment().remove(requestView.selectedReq.value.requestId)
                        }
                    }
                    this@RefuseWindow.close()
                }

                button("Annuler").action {
                    this@RefuseWindow.close()
                }

                alignment = Pos.BASELINE_CENTER
            }


            setMinSize(400.0, 250.0)
            setPrefSize(400.0, 250.0)
        }
}