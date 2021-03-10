package dbScripts

import database.DatabaseApi
import database.DatabaseApi.Documents.idDoc
import database.DatabaseApi.adminDB
import database.SeedPhrase
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import utils.SHA256.encrypt
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

fun generateCsv (filesNames: List<String>, str: String) {
    filesNames.forEach {
        File("files/mod$str").mkdir()
        transaction {
            val db = DatabaseApi.Students.select { DatabaseApi.Students.niveau eq "CP2" }.map{it[Expression.build { DatabaseApi.Students.studentId }]}
            FileOutputStream("files/mod$str/$it.csv").write(db.joinToString("\n") { x ->
                "$x,${
                    String.format(
                        "%.2f",
                        Random.nextDouble(10.25, 19.5)
                    )
                }"
            }.toByteArray())
        }
    }
}

fun main() {
    DatabaseApi.connectDB()
    /*transaction {
        //SchemaUtils.drop(DatabaseApi.Students)
        //SchemaUtils.create(DatabaseApi.Students)
        DatabaseApi.Students.deleteWhere { DatabaseApi.Students.firstName eq "" }

        val fileFirst = File("files/firstNames").readLines()
        val fileLast = File("files/lastNames").readLines()
        val fileVille = File("files/Ville").readLines()
        val villes = fileVille.map {it.takeWhile { c -> c != ' '}}
        val cins = fileVille.flatMap{it.split(" ").drop(1)}

        for (i in 0..6) {
            val f = fileFirst[nextInt(fileFirst.size)]
            val l = fileLast[nextInt(fileLast.size)]
            DatabaseApi.Students.insert {
                it[studentId] = nextInt(10000000, 99999999)
                it[firstName] = f
                it[lastName] = l
                it[niveau] = "CP2"//listOf("GI1", "GI2", "CP1", "CP2")[nextInt(3)]
                it[CIN] = "${cins[nextInt(cins.size)]}${nextInt(10000,99999)}"
                it[cne] = "p${nextInt(100000000,100999999)}"
                it[email] = "$f.$l@etu.uae.ac.ma"
                it[annee] = DateTime("${nextInt(1997,2000)}-${nextInt(1,12)}-${nextInt(1,25)}")
                it[ville] = villes[nextInt(villes.size)]
            }
        }
    }

    val filesNames = listOf("Algebre3",
        "Analyse3",
        "Info2",
        "Mecanique2",
        "Physique2",
        "TEC")
    generateCsv(filesNames, "CpS3")
    transaction {
        DatabaseApi.Modules.deleteWhere { DatabaseApi.Modules.niveau eq "CP2" }
        filesNames.map{File("files/modCpS3/$it.csv")}.zip(IntRange(1,6)).forEach { x ->
            DatabaseApi.Modules.insert {
                it[idModule] = "CpS3${x.second}"
                it[nomModule] = x.first.nameWithoutExtension
                it[fichierNotes] = x.first.readText()
                it[semestre] = 1
                it[niveau] = "CP2"
            }
        }
    }


    val filesNames2 = listOf(
        "Activite d'ouveture",
        "Analyse4",
        "Electronique",
        "Management",
        "Mathematique Appliquee",
        "Physique4")
    generateCsv(filesNames2, "CpS4")
    transaction {
        filesNames2.map{File("files/modCpS4/$it.csv")}.zip(IntRange(1,6)).forEach { x ->
            DatabaseApi.Modules.insert {
                it[idModule] = "CpS4${x.second}"
                it[nomModule] = x.first.nameWithoutExtension
                it[fichierNotes] = x.first.readText()
                it[semestre] = 2
                it[niveau] = "CP2"
            }
        }
    }*/

    transaction (adminDB) {
        //SchemaUtils.drop(DatabaseApi.Admins)
        //SchemaUtils.create(DatabaseApi.Admins)

        val se = SeedPhrase.generateSeed().joinToString(" ")
        print(se)
        DatabaseApi.Admins.insert {
            it[adminId] = "root ${"toor".encrypt()}".encrypt()
            it[account] = "root"
            it[password] = "toor".encrypt()
            it[email] = "silverest12@gmail.com"
            it[seed] = se.encrypt()
        }
    }

    /*transaction {
        //SchemaUtils.drop(DatabaseApi.Modules)
        //SchemaUtils.create(DatabaseApi.Modules)
        //DatabaseApi.Modules.deleteWhere {DatabaseApi.Modules.niveau eq "GI2"}

        File("files/modCpS3").listFiles().zip(IntRange(1, 6)).forEach { x ->
            DatabaseApi.Modules.insert {
                it[idModule] = "CPS3${x.second}"
                it[nomModule] = x.first.nameWithoutExtension
                it[fichierNotes] = x.first.readText()
                it[semestre] = 1
                it[niveau] = "CP2"
            }
        }

        File("files/modCpS4").listFiles().zip(IntRange(1, 6)).forEach { x ->
            DatabaseApi.Modules.insert {
                it[idModule] = "CPS4${x.second}"
                it[nomModule] = x.first.nameWithoutExtension
                it[fichierNotes] = x.first.readText()
                it[semestre] = 2
                it[niveau] = "CP2"
            }
        }
    }*/

    /*transaction {
        DatabaseApi.Documents.deleteWhere { idDoc eq 0 }
        DatabaseApi.Documents.deleteWhere { idDoc eq 2 }
        DatabaseApi.Documents.deleteWhere { idDoc eq 1 }

        DatabaseApi.Documents.insert {
            it[idDoc] = 0
            it[nomDoc] = "Releve de note"
            it[modelDoc] = File("files/ReleveDeNote/ReleveDeNote.tex").readText()
        }

        DatabaseApi.Documents.insert {
            it[idDoc] = 1
            it[nomDoc] = "Attestation de scolarite"
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
