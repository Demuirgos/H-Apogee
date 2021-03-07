package database

import adminSide.Request
import database.DatabaseApi.Documents.idDoc
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.jodatime.date
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.io.File


object DatabaseApi {

    fun connectDB () =
        Database.connect(
            "jdbc:mysql://localhost:3306/StudentService",
            driver= "com.mysql.cj.jdbc.Driver",
            user = "silverest", password = ""
        )

    object Admins : Table() {
        val adminId: Column<Int> = integer("id_admin").uniqueIndex()
        val account: Column<String> = varchar("account", 50)
        val password: Column<String> = varchar("password", 64)
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
        EmailSender.sendFile(req.email, "Votre document est joint ci-dessous.", File("files/temp/temp.pdf"))
        File("files/temp").deleteRecursively()
        req.addToDB(true)
    }

    fun sendRequestRefused (req: Request, msg: String) {
        EmailSender.sendFile(req.email, "Votre demande de document a été refuser.\nRaison : $msg", null)
        req.addToDB(false)
    }

}

fun main() {
    DatabaseApi.connectDB()
    /*transaction {
        //SchemaUtils.drop(DatabaseApi.Students)
        //SchemaUtils.create(DatabaseApi.Studen
        ts)
        DatabaseApi.Students.insert {
            it[studentId] = 123456789
            it[firstName] = "Ayman"
            it[lastName] = "Bouchareb"
            it[niveau] = "GI2"
            it[CIN] = "LF57626"
            it[cne] = "P1360778773"
            it[email] = "Ayman.Bouchareb@etu.uae.ac.ma"
            it[annee] = DateTime("1999-07-23")
            it[ville] = "Tetouan"
        }
    }*/

    /*transaction {
        SchemaUtils.drop(DatabaseApi.Modules)
        SchemaUtils.create(DatabaseApi.Modules)
        File("files/module1").listFiles().zip(IntRange(1,6)).forEach {x ->
            DatabaseApi.Modules.insert {
                it[idModule] = "GI2S3${x.second}"
                it[nomModule] = x.first.nameWithoutExtension
                it[fichierNotes] = x.first.readText()
                it[semestre] = 1
                it[niveau] = "GI2"
            }
        }

        File("files/module2").listFiles().zip(IntRange(1,6)).forEach {x ->
            DatabaseApi.Modules.insert {
                it[idModule] = "GI2S4${x.second}"
                it[nomModule] = x.first.nameWithoutExtension
                it[fichierNotes] = x.first.readText()
                it[semestre] = 2
                it[niveau] = "GI2"
            }
        }
    }*/

    /*transaction {
        DatabaseApi.Documents.deleteWhere { idDoc eq 0 }
        DatabaseApi.Documents.deleteWhere { idDoc eq 2 }
        DatabaseApi.Documents.deleteWhere { idDoc eq 1 }

        DatabaseApi.Documents.insert {
            it[idDoc] = 0
            it[nomDoc] = "Attestation de stage"
            it[modelDoc] = File("files/ReleveDeNote/ReleveDeNote.tex").readText()
        }

        DatabaseApi.Documents.insert {
            it[idDoc] = 1
            it[nomDoc] = "Attestation de stage"
            it[modelDoc] = File("files/AttestationDeScolarite/AttestationDeScolarite.tex").readText()
        }

        DatabaseApi.Documents.insert {
            it[idDoc] = 2
            it[nomDoc] = "Attestation de stage"
            it[modelDoc] = File("files/AttestationDeStage/AttestationDeStage.tex").readText()
        }
    }*/

    /*transaction {
        SchemaUtils.create(DatabaseApi.Requests)
    }*/
}

