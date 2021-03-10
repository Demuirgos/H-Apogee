package paperWork

import database.DatabaseApi
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import paperWork.Niveau.Companion.get
import java.time.LocalDate

object AttestationDeScolarite : PaperWork {
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
            val cin = studInfos?.get(Expression.build { DatabaseApi.Students.CIN }).toString()
            val cne = studInfos?.get(Expression.build { DatabaseApi.Students.cne }).toString()
            val villeNaiss = studInfos?.get(Expression.build { DatabaseApi.Students.ville }).toString()
            val dateNaiss = studInfos?.get(Expression.build { DatabaseApi.Students.annee})?.toLocalDate().toString()
            val niveau = studInfos?.get(Expression.build { DatabaseApi.Students.niveau }).toString()

            return@transaction getFile()
                .replaceFirst("yearArg", LocalDate.now().year.toString())
                .replaceFirst("nomArg", lastName)
                .replaceFirst("prenomArg", firstName)
                .replaceFirst("cinArg", cin)
                .replaceFirst("cneArg", cne)
                .replaceFirst("dateNaissArg", dateNaiss)
                .replaceFirst("villeNaissArg", villeNaiss)
                .replaceFirst("anneUnivArg",   "${LocalDate.now().year - 1} / ${LocalDate.now().year}" )
                .replaceFirst("anneeDipArg", Niveau.parseDip(niveau))
                .replace("anneeEtuArg", "${Niveau.parseNiv(niveau.last())} ann\\'ee")
                .replace("niveauArg", Niveau.parse(niveau).get().toString())
                .replace("fillArg", Niveau.parseFilliere(niveau))
                .replace("numApogeeArg", id.toString())
        }
}