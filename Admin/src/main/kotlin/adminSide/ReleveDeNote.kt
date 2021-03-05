package adminSide

import adminSide.ReleveDeNote.Niveau.Companion.get
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import database.DatabaseApi
import database.EmailSender
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import utils.Either
import java.io.File
import java.io.FileOutputStream

object ReleveDeNote : PaperWork {
    enum class Niveau {
        CP, CI;

        companion object {
            fun parse(niveau: String) =
                when {
                    niveau.matches(Regex("(GI|SCM|GSTR|GC|GM)[1-3]")) -> Either.Right(CI)
                    niveau.matches(Regex("CP[1-2]")) -> Either.Right(CP)
                    else -> Either.Left(Error("Niveau invalide"))
                }

            fun Either<Error, Niveau>.get () =
                when (this) {
                    is Either.Right -> this.value
                    is Either.Left -> throw this.value
                }
        }
    }

    private fun getFile(): String =
        transaction {
            DatabaseApi.Documents.select {
                DatabaseApi.Documents.idDoc eq 1
            }.firstOrNull()?.get(Expression.build { DatabaseApi.Documents.modelDoc })
        }.toString()

    override fun getData(id: Int) : String =
        transaction {
            val studInfos = DatabaseApi.Students.select { DatabaseApi.Students.studentId eq id }.firstOrNull()
            val firstName = studInfos?.get(Expression.build { DatabaseApi.Students.firstName }).toString()
            val lastName = studInfos?.get(Expression.build { DatabaseApi.Students.lastName }).toString()
            val niveau = studInfos?.get(Expression.build { DatabaseApi.Students.niveau }).toString()
            val cne = studInfos?.get(Expression.build { DatabaseApi.Students.cne }).toString()
            val annee = studInfos?.get(Expression.build { DatabaseApi.Students.annee })?.toLocalDate().toString()
            val ville = studInfos?.get(Expression.build { DatabaseApi.Students.ville }).toString()

            val modules = DatabaseApi.Modules.select { DatabaseApi.Modules.niveau eq niveau}

            val r = IntRange(1, 12).toList()
            val listModArg = r.map {"module${it}Arg"}
            val listNoteArg = r.map {"note${it}Arg"}
            val listResArg = r.map { "resultatSta${it}Arg" }
            val listJuryArg = r.map {"ptsJury${it}Arg"}

            val listMod = modules.map {it[Expression.build { DatabaseApi.Modules.nomModule }]}
            val notes = modules
                .map {it[Expression.build { DatabaseApi.Modules.fichierNotes }]}
                .map{ csvReader().readAll(it).first {x -> x.first().toInt() == id}[1]}

            return@transaction getFile()
                .replace("numEtuArg", id.toString())
                .replaceFirst("nomArg", lastName)
                .replaceFirst("prenomArg", firstName)
                .replaceFirst("cneArg", cne)
                .replaceFirst("dateNaissArg", annee)
                .replaceFirst("anneeEducArg", niveau)
                .replaceFirst("villeArg", ville)
                .listRep(listModArg, listMod)
                .listRep(listNoteArg, notes)
                .replaceFirst("noteFinArg", (notes.map{it.toInt()}.sum() / 12).toString())
                .listRep(listResArg, notes.map { stateRes(it.toInt(), Niveau.parse(niveau).get())})
                .listRep(listJuryArg, r.map {""})
                .replaceFirst("sessionArg", "session 1")
        }

    private fun stateRes(x: Int, niveau: Niveau): String =
        when (niveau) {
            Niveau.CI ->
                if (x >= 12)
                    "valider"
                else
                    "non valider"

            Niveau.CP ->
                if (x >= 10)
                    "valider"
                else
                    "non valider"
        }

    private fun String.listRep(xs: List<String>, rs: List<String>) : String=
        if (xs.isEmpty() || rs.isEmpty()) {
            this
        }
        else
            this.replaceFirst(xs.first(), rs.first()).listRep(xs.drop(1),rs.drop(1))
}