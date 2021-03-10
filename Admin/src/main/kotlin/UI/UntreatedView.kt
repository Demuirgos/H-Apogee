package UI

import database.LogSession
import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import tornadofx.*
import kotlin.system.exitProcess

class UntreatedView : View("Requetes Non-traiter") {
    private val requestView: RequestViewModel by inject()

    override val root =
        borderpane {

            left = vbox { add(RequestListFragment::class) }

            center = borderpane {

                center = vbox {
                    add(RequestDetailsFragment::class)
                }

                bottom = vbox {
                    vbox {

                        hbox {

                            button("accepter") {
                                action {
                                    alert(Alert.AlertType.CONFIRMATION, "Voulez-vous acceptez la requete?") {
                                        if (it == ButtonType.OK) {
                                            val req = requestView.selectedReq.value
                                            runAsync {
                                                LogSession.get().approve(req, "", true)
                                                ui {
                                                    RequestListFragment().remove(req.requestId)
                                                }
                                            }
                                        }

                                        useMaxWidth
                                    }
                                }
                            }


                            vbox {
                                button("refuser") {
                                    action {
                                        find(RefuseWindow::class).openWindow()
                                    }
                                    useMaxWidth
                                }
                                paddingLeft = 10
                            }

                            paddingBottom = 5
                            alignment = Pos.CENTER
                        }
                    }
                }

            }
            setMinSize(600.0, 300.0)
            setPrefSize(700.0, 400.0)
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

    override fun onDock() {
        super.onDock()
        RequestListFragment().reload()
    }

    override fun onDelete() {
        RequestListFragment().delete()
    }

    override fun onRefresh() {
        RequestListFragment().load()
    }
}