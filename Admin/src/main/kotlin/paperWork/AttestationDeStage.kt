package paperWork

import database.DatabaseApi
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import paperWork.Niveau.Companion.get
import java.io.FileOutputStream
import java.time.LocalDate

object AttestationDeStage : PaperWork {
    private fun getFile(): String =
        transaction {
            DatabaseApi.Documents.select {
                DatabaseApi.Documents.idDoc eq 2
            }.firstOrNull()?.get(Expression.build { DatabaseApi.Documents.modelDoc })
        }.toString()

    override fun getData(id: Int) : String =
        transaction {
            val studInfos = DatabaseApi.Students.select { DatabaseApi.Students.studentId eq id }.firstOrNull()
            val firstName = studInfos?.get(Expression.build { DatabaseApi.Students.firstName }).toString()
            val lastName = studInfos?.get(Expression.build { DatabaseApi.Students.lastName }).toString()
            val niveau = studInfos?.get(Expression.build { DatabaseApi.Students.niveau }).toString()

            return@transaction getFile()
                .replaceFirst("yearArg", LocalDate.now().year.toString())
                .replaceFirst("nomArg", lastName)
                .replaceFirst("prenomArg", firstName)
                .replace("anNumArg", Niveau.parseNiv(niveau.last()))
                .replace("niveauArg", Niveau.parse(niveau).get().toString())
                .replace("filliereArg", Niveau.parseFilliere(niveau))
        }

}