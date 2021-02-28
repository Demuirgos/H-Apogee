package UI

import adminSide.Request
import adminSide.Session
import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Pos
import javafx.scene.text.FontWeight
import tornadofx.*

class AppScreen : View() {
    private val rqs: Requests by inject()
    private val req: SimpleObjectProperty<Request> = SimpleObjectProperty(Request.Empty())

    private val name = req.stringBinding{ it?.firstName }

    override val root =
        borderpane {
            left = listview(rqs.values) {
                cellFormat {
                    graphic = cache {
                        form {
                            fieldset {
                                field("Nom") {
                                    label("${it.firstName} ${it.lastName}")
                                }

                                field("Date") {
                                    label(it.date)
                                }

                                label(it.docType) {
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
                onLeftClick {
                    runAsync {
                        req.set(selectedItem)
                    }
                }
            }


            center =  vbox  {

                hbox {
                    label("Prenom:")
                    text(req.stringBinding{it?.firstName})
                }

                hbox {
                    label("Nom:")
                    text(req.stringBinding{it?.lastName})
                }

                hbox {
                    label("Email:")
                    text(req.stringBinding{it?.email})
                }

                hbox {
                    label("ID:")
                    text(req.stringBinding{it?.id})
                }

                hbox {
                    label("CIN:")
                    text(req.stringBinding{it?.cin})
                }

                hbox {
                    label("Date:")
                    text(req.stringBinding{it?.date})
                }

                hbox {
                    label("Type Document:")
                    text(req.stringBinding{ it?.docType})
                }

                hbox {
                    vbox {
                        button("accepter") {
                            action {
                                Session.get().approve(true)
                            }
                        }
                    }

                    vbox {
                        button("refuser") {
                            action {
                                Session.get().approve(false)
                            }
                        }
                        paddingLeft = 10
                    }

                    paddingBottom = 10
                    paddingTop = 10
                    alignment = Pos.CENTER
                }

                button("se deconnecter") {
                    val l = LoginController()
                    action {
                        run {
                            l.logout()
                        }
                    }

                    alignment = Pos.CENTER
                }

                alignment = Pos.TOP_CENTER
                paddingTop = 10.0
                paddingLeft = 10.0
            }
        }
}

