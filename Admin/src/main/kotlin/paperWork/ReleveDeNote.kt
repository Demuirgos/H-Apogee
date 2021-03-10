package paperWork

import paperWork.Niveau.Companion.get
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import database.DatabaseApi
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object ReleveDeNote : PaperWork {
    private fun getFile(): String =
        transaction {
            DatabaseApi.Documents.select {
                DatabaseApi.Documents.idDoc eq 0
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
            val modules = DatabaseApi.Modules
                .select { DatabaseApi.Modules.niveau eq niveau }

            val modulesS1 = modules.filter {it[Expression.build { DatabaseApi.Modules.semestre }] == 1}
            val modulesS2 = modules.filter {it[Expression.build { DatabaseApi.Modules.semestre }] == 2}

            val r = IntRange(1, 12).toList()
            val listModArg = r.map {"module${it}Arg"}
            val listNoteArg = r.map {"note${it}Arg"}
            val listResArg = r.map { "resultatSta${it}Arg" }
            val listJuryArg = r.map {"ptsJury${it}Arg"}

            val listModS1 = modulesS1.map {it[Expression.build { DatabaseApi.Modules.nomModule }]}

            val listModS2 = modulesS2.map {it[Expression.build { DatabaseApi.Modules.nomModule }]}

            val notesS1 = modulesS1
                .map {it[Expression.build { DatabaseApi.Modules.fichierNotes }]}
                .map{ csvReader().readAll(it).first {x -> x.first().toInt() == id}[1]}

            val notesS2 = modulesS2
                .map {it[Expression.build { DatabaseApi.Modules.fichierNotes }]}
                .map{ csvReader().readAll(it).first {x -> x.first().toInt() == id}[1]}

            val notes = notesS1.plus(notesS2)

            return@transaction getFile()
                .replace("numEtuArg", id.toString())
                .replaceFirst("nomArg", lastName)
                .replaceFirst("prenomArg", firstName)
                .replaceFirst("cneArg", cne)
                .replaceFirst("dateNaissArg", annee)
                .replaceFirst("anneeEducArg", niveau)
                .replaceFirst("villeArg", ville)
                .listRep(listModArg, listModS1.plus(listModS2))
                .listRep(listNoteArg, notesS1.plus(notesS2))
                .replaceFirst("noteFinArg", String.format("%.2f", notes.map{it.toDouble()}.sum() / 12.0))
                .listRep(listResArg, notes.map { stateRes(it.toDouble(), Niveau.parse(niveau).get()) })
                .listRep(listJuryArg, IntRange(0,12).map{""})
                .replace("sessionArg", "session 1")
        }

    private fun stateRes(x: Double, niveau: Niveau): String =
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


}

