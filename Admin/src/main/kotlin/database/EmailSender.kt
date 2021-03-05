package database

import java.io.File
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

object EmailSender {
    private const val from = "project.gl.emailSender@gmail.com"
    private val props = System.getProperties()
    private const val smtpServer = "smtp.gmail.com"
    private const val username = "project.gl.emailSender@gmail.com"
    private const val password = "Genie12345"

    fun sendFile (sendTo: String, msg: String, file: File?) {
        props["mail.smtp.host"] = smtpServer
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.smtp.port"] = "587"

        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })

        try {
            val message: Message = MimeMessage(session)
            val textPart = MimeBodyPart()
            val filePart = MimeBodyPart()
            val multipart: Multipart = MimeMultipart()

            message.setFrom(InternetAddress(from))

            textPart.setText(msg)
            multipart.addBodyPart(textPart)

            if (file != null && file.exists()) {
                val fileSource = FileDataSource(file.absolutePath)
                filePart.dataHandler = DataHandler(fileSource)
                filePart.fileName = file.name
                multipart.addBodyPart(filePart)
            }

            message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(sendTo)
            )

            message.subject = "Réponse : Requête"

            message.setContent(multipart)

            Transport.send(message)

            println("Sent message successfully....")
        } catch (e: MessagingException) {
            e.printStackTrace()
        }

    }
}