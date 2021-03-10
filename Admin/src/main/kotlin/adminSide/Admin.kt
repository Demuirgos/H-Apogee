package adminSide

import database.DatabaseApi
import database.DatabaseApi.Admins
import database.DatabaseApi.adminDB
import database.LogSession
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import utils.Either
import utils.SHA256.encrypt

class Admin private constructor (private val key: String) {

    companion object {
        fun authenticate(account: String, pwd: String) =
            transaction (adminDB) {
                val accId = Admins
                    .select { Admins.account.eq(account) }
                    .having { Admins.password eq pwd.encrypt() }
                    .toList().firstOrNull()?.get(Expression.build { Admins.adminId })

                when {
                    accId != null -> {
                        Either.Right(LogSession.startSession(Admin(accId)))
                    }
                    Admins.select{Admins.account eq account}.firstOrNull() == null -> Either.Left(Error("Compte invalide"))
                    else -> Either.Left(Error("Mot de passe invalide"))
                }
            }


        fun empty() =
            Admin("")
    }

    fun approve(req: Request, msg: String, b: Boolean) {
        if (key.isNotEmpty()) {
            when (b) {
                true -> DatabaseApi.sendRequestAccepted(req, "")
                false -> DatabaseApi.sendRequestRefused(req, msg)
            }
        }
    }

    fun getId() = key

    override fun toString(): String =
        """Account Name: $key
        """.trimMargin()
}