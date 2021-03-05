package UI

import javafx.scene.text.FontWeight
import tornadofx.*


class RequestDetailsFragment : Fragment() {
    private val requestViewModel: RequestViewModel by inject()

    override val root = vbox {
        hbox(5) {
            label(title) {
                text = "Détails de la requête:"
                style{
                    fontSize = 22.px
                    fontWeight = FontWeight.BOLD
                }
            }
        }

        hbox(5) {
            label("Prenom:")
            text(requestViewModel.selectedFirstName)
            style {
                fontSize=16.px
            }
        }

        hbox(5) {
            label("Nom:")
            text(requestViewModel.selectedLastName)
            style {
                fontSize=16.px
            }
        }

        hbox(5) {
            label("Email:")
            text(requestViewModel.selectedEmail)

            style {
                fontSize=16.px
            }
        }

        hbox(5) {
            label("Numero:")
            text(requestViewModel.selectedId)

            style {
                fontSize=16.px
            }
        }

        hbox(5) {
            label("CNE:")
            text(requestViewModel.selectedCne)

            style {
                fontSize=16.px
            }
        }

        hbox(5) {
            label("CIN:")
            text(requestViewModel.selectedCin)

            style {
                fontSize=16.px
            }
        }

        hbox(5) {
            label("Date:")

            text(requestViewModel.selectedDate)

            style {
                fontSize=16.px
            }
        }

        hbox(5) {
            label("Type Document:")

            text(requestViewModel.selectedType)

            style {
                fontSize=16.px
            }
        }
        paddingAll = 5.0

        style {
            fontSize=16.px
        }
    }
}
