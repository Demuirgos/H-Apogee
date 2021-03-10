package UI

import database.SeedPhrase
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.Parent
import tornadofx.*

class PasswordForgottenView : View("Mot de passe Oublier") {
    private val model: ViewModel = ViewModel()
    private val nomCompte = model.bind { SimpleStringProperty() }
    private val pw1Field = model.bind { SimpleStringProperty() }
    private val pw2Field = model.bind { SimpleStringProperty() }
    private val pw3Field = model.bind { SimpleStringProperty() }
    private val pw4Field = model.bind { SimpleStringProperty() }
    private val pw5Field = model.bind { SimpleStringProperty() }
    private val pw6Field = model.bind { SimpleStringProperty() }

    override val root: Parent = vbox (5) {
        paddingAll = 5.0

        label("Nom de compte:")
        textfield(nomCompte) {
            required()
        }

        label("Entrer la phrase secrète")

        hbox (5) {
            textfield(pw1Field) {
                required()
                setPrefSize(200.0, 20.0)
            }

            textfield(pw2Field) {
                required()
                setPrefSize(200.0, 20.0)
            }

            alignment = Pos.BASELINE_CENTER
        }

        hbox (5) {
            textfield(pw3Field) {
                required()
                setPrefSize(200.0, 20.0)
            }

            textfield(pw4Field) {
                required()
                setPrefSize(200.0, 20.0)
            }

            alignment = Pos.BASELINE_CENTER
        }

        hbox (5) {
            textfield(pw5Field) {
                required()
                setPrefSize(200.0, 20.0)
            }

            textfield(pw6Field) {
                required()
                setPrefSize(200.0, 20.0)
            }

            alignment = Pos.BASELINE_CENTER
        }

        hbox(5) {
            button("Confirmer") {
                action {
                    runAsync {
                        SeedPhrase.checkSeedPhrase(nomCompte.value,
                            listOf(
                                pw1Field,
                                pw2Field,
                                pw3Field,
                                pw4Field,
                                pw5Field,
                                pw6Field
                            ).joinToString(" ") { it.value }
                        )
                    }
                    this@PasswordForgottenView.close()
                    shortcut("enter")
                }
            }

            button("Annuler")
            {
                action {
                    this@PasswordForgottenView.close()
                    shortcut("esc")
                }
            }

            alignment = Pos.BASELINE_CENTER

        }


        alignment = Pos.BASELINE_CENTER
        setMinSize(420.0, 240.0)
        setPrefSize(420.0, 240.0)
        setMaxSize(420.0, 240.0)

        usePrefSize = true
    }
}
