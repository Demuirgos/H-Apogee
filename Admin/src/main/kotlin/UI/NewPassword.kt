package UI

import database.SeedPhrase
import database.LogSession
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.Alert
import tornadofx.*

class NewPassword : View() {
   private val model: ViewModel = ViewModel()
   private val pw1Field = model.bind { SimpleStringProperty() }
   private val pw2Field = model.bind { SimpleStringProperty() }
   private val pw3Field = model.bind { SimpleStringProperty() }
   private val pw4Field = model.bind { SimpleStringProperty() }
   private val pw5Field = model.bind { SimpleStringProperty() }
   private val pw6Field = model.bind { SimpleStringProperty() }

   override val root: Parent = vbox {
      label("Entrer la phrase secrète")

      hbox(5) {
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

      hbox(5) {
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

      hbox(5) {
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
         paddingTop = 5
         button("Confirmer") {
            action {
               if (SeedPhrase.isRight(LogSession.getKey(),
                     listOf(
                        pw1Field,
                        pw2Field,
                        pw3Field,
                        pw4Field,
                        pw5Field,
                        pw6Field
                     ).joinToString(" ") { it.value })
               ) {
                  find(NewPassword::class).replaceWith(ChangePassword::class, centerOnScreen = true)
               } else
                  alert(Alert.AlertType.INFORMATION, "Phrase secrete fausse")

            }

            shortcut("enter")
         }


         button("Annuler") {
            action {
               this@NewPassword.close()

               shortcut("esc")
            }
         }
         alignment = Pos.BASELINE_CENTER
      }

      alignment = Pos.BASELINE_CENTER
      setMinSize(420.0, 200.0)
      setPrefSize(420.0, 200.0)
      setMaxSize(420.0, 200.0)

      usePrefSize = true
   }

}