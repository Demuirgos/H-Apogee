package database

import adminSide.Request
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
        val idModule: Column<Int> = integer("id_module").uniqueIndex()
        val nomModule: Column<String> = varchar("nom_module",255)
        val niveau: Column<String> = varchar("niveau", 20)
        val fichierNotes: Column<String> = text("fichier_note")
    }

    object Documents: Table() {
        val idDoc: Column<Int> = integer("id_doc").uniqueIndex()
        val nomDoc: Column<String> = varchar("nom_doc", 50)
        val modelDoc: Column<String> = text("model_doc")
    }

    fun sendRequestAccepted (req: Request) {
        FileGenerator.createFile(req.docType, req.id)
        EmailSender.sendFile(req.email, "Votre document est joint ci-dessous.", File("files/temp/temp.pdf"))
        File("files/temp").deleteRecursively()
    }

    fun sendRequestRefused (req: Request) {
        EmailSender.sendFile(req.email, "Votre demande de document a été refuser.", null)
    }
}

fun main() {
    DatabaseApi.connectDB()
    /*transaction {
        SchemaUtils.drop(DatabaseApi.Students)
        SchemaUtils.create(DatabaseApi.Students)
        DatabaseApi.Students.insert {
            it[studentId] = 16039428
            it[firstName] = "Taha"
            it[lastName] = "metougui"
            it[niveau] = "GI2"
            it[CIN] = "L610332"
            it[cne] = "P100086303"
            it[email] = "taha.metougui@etu.uae.ac.ma"
            it[annee] = DateTime("1998-02-23")
            it[ville] = "Tetouan"
        }
    }*/

    transaction {
        /*SchemaUtils.drop(DatabaseApi.Modules)
        SchemaUtils.create(DatabaseApi.Modules)
        File("files/module").listFiles().zip(IntRange(1,12)).forEach {x ->
            DatabaseApi.Modules.insert {
                it[idModule] = x.second
                it[nomModule] = x.first.nameWithoutExtension
                it[fichierNotes] = x.first.readText()
                it[niveau] = "GI2"
            }
        }*/
        print(DatabaseApi.Modules.select { DatabaseApi.Modules.niveau eq "GI2"}.map {it[Expression.build { DatabaseApi.Modules.fichierNotes }]})
    }
}

