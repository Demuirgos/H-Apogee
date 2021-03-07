package adminSide

import database.DatabaseApi
import database.DatabaseApi.Admins
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import utils.Either
import utils.SHA256.encrypt

class Admin private constructor (private val key: String) {

    companion object {
        fun authenticate(account: String, pwd: String) =
            transaction {
                val accId = Admins
                    .select { Admins.account.eq(account) }
                    .having { Admins.password eq pwd.encrypt() }
                    .toList().firstOrNull()?.get(Expression.build { Admins.adminId })

                if (accId != null) {
                    Either.Right(Session.startSession(Admin("$accId $account $pwd".encrypt())))
                } else
                    Either.Left(Error("Compte invalide"))
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

    override fun toString(): String =
        """Account Name: $key
        """.trimMargin()
}