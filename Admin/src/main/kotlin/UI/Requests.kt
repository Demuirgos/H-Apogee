package UI

import adminSide.Request
import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.text.FontWeight
import tornadofx.*

class Requests: Controller() {
    val values: ObservableList<Request> =
        listOf(
            Request("Taha", "Metougui", "silverest12@gmail.com", "L213", "L610332", "02/28/2021", "Attestation"),
            Request("John", "Doe", "john.doe@gmail.com", "R384", "L641302", "02/28/2021", "Releve de note")
        ).toObservable()
}