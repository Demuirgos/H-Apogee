package App

import UI.LoginScreen
import database.DatabaseApi
import javafx.application.Platform
import javafx.stage.Stage
import tornadofx.*
import kotlin.system.exitProcess

class AdminApp : App(LoginScreen::class) {
    override fun start (stage: Stage) {

        with (stage) {
            minWidth = 350.0
            minHeight = 400.0
            super.start(this)
        }

    }

    override fun stop() {
        Platform.exit()
        exitProcess(0)
    }
}

fun main() {
    DatabaseApi.connectDB()
    launch <AdminApp>()
}