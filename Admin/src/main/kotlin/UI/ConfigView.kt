package UI

import database.DatabaseApi
import database.LogSession
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.image.Image
import javafx.scene.text.FontWeight
import tornadofx.*
import java.io.FileInputStream

class ConfigView : View() {
    override val root: Parent =
        vbox(10) {

            imageview {
                paddingTop = 10.0
                image = Image(FileInputStream("path2192.png"))
                isSmooth
                paddingBottom = 15.0
            }

            label("ID").style {
                fontSize = 16.px
                fontWeight = FontWeight.BOLD
            }

            val str = LogSession.getKey().substring(16)

            text(str) {
                maxWidth(100.0)
                useMaxWidth
            }

            label("Nom").style {
                fontSize = 16.px
                fontWeight = FontWeight.BOLD
            }

            text("${LogSession.getAdminName()}") {
                maxWidth(100.0)
                useMaxWidth
            }

            button("Changer le mot de passe").action {
                find(NewPassword::class).openWindow()
            }
            alignment = Pos.BASELINE_CENTER


            button("Generer une phrase secrete").action {
                find(NewSeed::class).openWindow()
            }

            button("Exporter les requetes traiter").action {
                chooseDirectory()?.absolutePath?.let { DatabaseApi.exportReq(it) }
            }
        }

}


