package UI

import database.LoginController
import javafx.scene.image.Image
import javafx.scene.text.FontWeight
import tornadofx.*
import java.io.FileInputStream

class AppWorkSpace : Workspace() {
    val i: Int by inject()

    init {

        dock<UntreatedView>()

        button {
            action {
                LoginController().logout()
            }
            shortcut("ctrl + d")
            style {
                imageview(Image(FileInputStream("files/icons/logout.png")))
                backgroundColor += c("#ececec")

            }
        }


        //item ("Requites"){
        menubutton {

            style {
                imageview(Image(FileInputStream("files/icons/menu.png")))
            }

            item("Non-traiter") {
                style {
                    fontWeight = FontWeight.BOLD
                }

                action {
                    dock<UntreatedView>()
                }
            }

            item("Traiter") {

                style {
                    fontWeight = FontWeight.BOLD
                }
                action {
                    dock<TreatedView>()

                }
            }
        }

        button {
            action {
                dock <ConfigView>()
            }

            shortcut("alt + c")

            style {
                imageview(Image(FileInputStream("files/icons/config.png")))
                backgroundColor += c("#ececec")

            }
        }
    }

    override fun onDock() {
        backButton.hide()
        forwardButton.hide()
        saveButton.hide()
        createButton.hide()
    }
}