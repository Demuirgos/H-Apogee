package UI

import adminSide.Admin
import adminSide.Session
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import tornadofx.*
import utils.Either

class LoginController: Controller() {
    private val statusProperty = SimpleStringProperty("")

    fun login (username: String, password: String) {
        runLater { statusProperty.set("") }
        val response = Admin.authenticate(username, password)
        runLater {
            when (response) {
                is Either.Right -> {
                    find(LoginScreen::class).replaceWith(AppWorkSpace::class, centerOnScreen = true, sizeToScene = true)
                }
                is Either.Left -> statusProperty.set("Invalid account")
            }
        }
    }

    fun logout() {
        alert(Alert.AlertType.CONFIRMATION, "Se deconnecter?") {
            if (it == ButtonType.OK) {
                Session.disconnect()
                primaryStage.uiComponent<UIComponent>()
                    ?.replaceWith(LoginScreen::class, sizeToScene = true, centerOnScreen = true)
            }

            height = 50.0
            width = 100.0
        }
    }
}