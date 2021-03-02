package database

import adminSide.Request
import googleDrive.GoogleConn
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table
import java.io.File

object DatabaseApi {

    fun connectDB () =
        Database.connect(
            "jdbc:mysql://localhost:3306/StudentService",
            driver= "com.mysql.cj.jdbc.Driver",
            user = "silverest", password = ""
        )

    object Admins : Table() {
        val adminId: Column<Int> = integer("admin_id").uniqueIndex()
        val account: Column<String> = varchar("account", 50)
        val password: Column<String> = varchar("password", 64)
    }

    object Students: Table() {
        val studentId: Column<Int> = integer("student_id").uniqueIndex()
        val firstName: Column<String> = varchar("first_name", 20)
        val lastName: Column<String> = varchar("last_name", 20)
        val CIN: Column<String> = varchar("CIN", 10)
        val email: Column<String> = varchar("email", 255)
    }

    object Requests : Table() {
        val reqId: Column<Int> = integer("request_id").autoIncrement()
        val content: Column<String> = text("content")
    }

    fun sendRequestAccepted (req: Request) {
        FileGenerator.createFile(req.docType)
        Runtime.getRuntime().exec("pdfGen.sh")
        EmailSender.sendFile(req.email, "Votre document est joint ci-dessous.", File("/files/temp/temp.pdf"))
        Runtime.getRuntime().exec("rm files/temp/*")
    }

    fun sendRequestRefused (req: Request) {
        EmailSender.sendFile(req.email, "Votre demande de document a été refuser.", null)
    }
}

