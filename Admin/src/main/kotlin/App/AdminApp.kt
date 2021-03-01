package App

import UI.LoginScreen
import database.DatabaseApi
import javafx.stage.Stage
import tornadofx.*

class AdminApp : App(LoginScreen::class) {
    override fun start (stage: Stage) {

        with (stage) {
            minWidth = 350.0
            minHeight = 400.0
            super.start(this)
        }

    }
}

fun main() {
    DatabaseApi.connectDB()
    launch <AdminApp>()
}