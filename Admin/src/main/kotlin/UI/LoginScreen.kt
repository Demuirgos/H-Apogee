package UI

import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.image.Image
import tornadofx.*
import java.io.FileInputStream

class LoginScreen : View("Login") {
    private val model = ViewModel()

    private val username = model.bind {SimpleStringProperty()}
    private val password = model.bind {SimpleStringProperty()}
    private val loginController: LoginController by inject()

    override val root = form {

        imageview{
            paddingTop = 10.0
            image = Image(FileInputStream("path2192.png"))
            isSmooth
            paddingBottom = 15.0
        }

        fieldset ("Compte") {
            textfield(username).required()

            setMaxSize(300.0, 20.0)
        }

        fieldset ("Mot de passe") {
            passwordfield(password).required()
            setMaxSize(300.0, 20.0)
        }

        hbox {
            vbox {
                button("Se connecter") {
                    enableWhen(model.valid)
                    isDefaultButton = true
                    useMaxHeight = true

                    action {
                        runAsyncWithProgress {
                            loginController.login(username.value, password.value)
                        }
                    }

                    shortcut("enter")
                }
            }

            vbox {
                button ("Mot de passe oublier") {

                }
                paddingLeft = 10.0
            }
            alignment = Pos.CENTER
            paddingBottom = 10.0
        }

        button("quitter") {
            useMaxHeight = true

            action {
                UntreatedView().exit()
            }

            shortcut("ctrl + q")
        }

        alignment = Pos.CENTER

        setPrefSize(250.0, 350.0)
    }
}