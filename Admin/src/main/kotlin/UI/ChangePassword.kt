package UI

import database.DatabaseApi
import javafx.beans.property.SimpleStringProperty
import javafx.scene.Parent
import javafx.scene.control.Alert
import tornadofx.*

class ChangePassword : View() {

    private val model = ViewModel()

    private val pwredo = model.bind { SimpleStringProperty() }
    private val password = model.bind { SimpleStringProperty() }

    override val root: Parent = form {
        fieldset("Mot de passe") {
            passwordfield(password).required()

            setMaxSize(300.0, 20.0)
        }

        fieldset("Reentrez le Mot de passe") {
            passwordfield(pwredo).required()
            setMaxSize(300.0, 20.0)
        }

        button("Confirmer") {
            action {
                if (pwredo.value == password.value) {
                    DatabaseApi.modAdminPwd(pwredo.value)
                    alert(Alert.AlertType.INFORMATION, "Mot de passe modifier avec succes")
                    close()
                } else {
                    alert(Alert.AlertType.INFORMATION, "Les champs de sont pas identiques")
                }
            }
            shortcut("enter")
        }

        button("Annuler") {
            action {
                close()
            }
            shortcut("esc")
        }
    }
}