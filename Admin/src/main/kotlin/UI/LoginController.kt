package UI

import adminSide.Admin
import adminSide.Session
import javafx.beans.property.SimpleStringProperty
import tornadofx.Controller
import tornadofx.UIComponent
import tornadofx.runLater
import tornadofx.uiComponent
import utils.Either

class LoginController: Controller() {
    private val statusProperty = SimpleStringProperty("")

    fun login (username: String, password: String) {
        runLater { statusProperty.set("") }
        val response = Admin.authenticate(username, password)
        runLater {
            when (response) {
                is Either.Right -> {
                    Session.startSession(response.value)
                    find(LoginScreen::class).replaceWith(AppScreen::class, sizeToScene = true, centerOnScreen = true)
                }
                is Either.Left -> statusProperty.set("Invalid account")
            }
        }
    }

    fun logout() {
        Session.disconnect()
        primaryStage.uiComponent<UIComponent>()?.replaceWith(LoginScreen::class, sizeToScene = true, centerOnScreen = true)
    }
}