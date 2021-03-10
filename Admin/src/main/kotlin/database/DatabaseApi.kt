package database

import adminSide.Request
import adminSide.TreatedRequest
import database.DatabaseApi.Admins.adminId
import database.DatabaseApi.Requests.etatRequest
import database.DatabaseApi.Requests.fichierReq
import database.DatabaseApi.Requests.idRequest
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.jodatime.date
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import utils.Either
import utils.SHA256.encrypt
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random.Default.nextDouble


object DatabaseApi {

    fun connectDB () =
        Database.connect(
            "jdbc:mysql://localhost:3306/StudentService",
            driver= "com.mysql.cj.jdbc.Driver",
            user = "silverest", password = ""
        )

    val adminDB =
        Database.connect(
            "jdbc:mysql://localhost:3306/AdminDB",
            driver= "com.mysql.cj.jdbc.Driver",
            user = "silverest", password = ""
        )

    object Admins : Table() {
        val adminId: Column<String> = varchar("id_admin",64).uniqueIndex()
        val account: Column<String> = varchar("account", 50)
        val password: Column<String> = varchar("password", 64)
        val email: Column<String> = varchar("email",256)
        val seed: Column<String> = varchar("seed", 64)
    }

    object Students: Table() {
        val studentId: Column<Int> = integer("id_student").uniqueIndex()
        val cne: Column<String> = varchar("CNE", 12)
        val CIN: Column<String> = varchar("CIN", 10)
        val firstName: Column<String> = varchar("first_name", 20)
        val lastName: Column<String> = varchar("last_name", 20)
        val niveau: Column<String> = varchar("niveu", 20)
        val email: Column<String> = varchar("email", 255)
        val annee: Column<DateTime> = date("annee_naissance")
        val ville: Column<String> = varchar("ville", 255)
    }

    object Modules: Table() {
        val idModule: Column<String> = varchar("id_module",10).uniqueIndex()
        val nomModule: Column<String> = varchar("nom_module",255)
        val niveau: Column<String> = varchar("niveau", 20)
        val semestre: Column<Int> = integer("semestre")
        val fichierNotes: Column<String> = text("fichier_note")
    }

    object Documents: Table() {
        val idDoc: Column<Int> = integer("id_doc").uniqueIndex()
        val nomDoc: Column<String> = varchar("nom_doc", 50)
        val modelDoc: Column<String> = text("model_doc")
    }

    object Requests: Table() {
        val idRequest: Column<String> = varchar("id_request", 256).uniqueIndex()
        val studentId: Column<Int> = integer("id_student")
        val fichierReq: Column<String> = text("ficher_json")
        val etatRequest: Column<Boolean> = bool("etat_requete")
    }

    fun sendRequestAccepted (req: Request, msg: String) {
        FileGenerator.createFile(req.docType, req.id)
        EmailSender.sendFile(req.email, "[AdminAccept] ${req.docType}", "Votre document est joint ci-dessous.",File("files/temp/temp.pdf"))
        File("files/temp").deleteRecursively()
        req.addToDB(true)
    }

    fun sendRequestRefused (req: Request, msg: String) {
        EmailSender.sendFile(req.email, "[AdminRefuse] ${req.docType}","Votre demande de document a été refuser.\nRaison : $msg", null)
        req.addToDB(false)
    }

    fun getReqs() = transaction {
        val xs = Requests.selectAll()
        return@transaction xs.map {
            val f = Request.fromJson(
                it[Expression.build { fichierReq }],
                it[Expression.build { idRequest }]
            )
            TreatedRequest.fromRequest(
                when (f) {
                    is Either.Right -> f.value!!
                    else -> Request.empty()
                }, it[Expression.build { etatRequest }]
            )
        }
    }

    fun delReq(reqId: String) = transaction {
        Requests.deleteWhere {
            idRequest eq reqId
        }
    }

    fun exportReq(path: String) {
        FileOutputStream("$path/dataRequetes.csv")
            .write(getReqs().joinToString("\n") { it.toCsv() }.toByteArray())
   }

    fun modAdminPwd(pwredo: String) {
        transaction (adminDB) {
            Admins.update ({
                adminId eq LogSession.getKey()
            }) { it[password] = pwredo.encrypt()}
        }
    }

}

