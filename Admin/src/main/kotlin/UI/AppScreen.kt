package UI

import adminSide.Session
import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.text.Font
import tornadofx.*
import kotlin.system.exitProcess

class AppScreen : View() {
    private val requestView: RequestViewModel by inject()

    override val root =
        borderpane {

            top {
                label(title) {
                    font = Font.font(22.0)
                }

                menubar {
                    menu("Fichier") {
                        item("Se deconnecter", "Shortcut+d").action {
                            LoginController().logout()
                        }

                        item("Quitter", "Shortcut+q").action {
                            exit()
                        }
                    }
                }
            }

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
                                    alert(Alert.AlertType.CONFIRMATION, "Voulez-vous supprimez?") {
                                        if (it == ButtonType.OK) {
                                            runAsync {
                                                Session.get().approve(requestView.selectedReq.value, true)
                                            }
                                        }

                                        useMaxWidth
                                    }
                                }
                            }


                            vbox {
                                button("refuser") {
                                    action {
                                        runAsync {
                                            Session.get().approve(requestView.selectedReq.value, false)
                                        }
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
            setMinSize(600.0, 400.0)
            setPrefSize(700.0, 500.0)
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
}