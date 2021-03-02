package adminSide

import database.DatabaseApi
import database.DatabaseApi.Admins
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import utils.Either
import utils.SHA256.encrypt

class Admin private constructor (private val key: String) {

    companion object {
        fun authenticate(account: String, pwd: String) = run {
            val acc = transaction {
                Admins
                    .select{Admins.account.eq(account)}
                    .having{Admins.password eq pwd.encrypt()}
                    .firstOrNull()
            }

            if (acc != null)
                Either.Right(Admin("$account $pwd".encrypt()))
            else
                Either.Left(Error("Compte invalide"))
        }

        fun empty() =
            Admin("")
    }

    fun approve(req: Request, b: Boolean) {
        if (key.isNotEmpty()) {
            when (b) {
                true -> DatabaseApi.sendRequestAccepted(req)
                false -> DatabaseApi.sendRequestRefused(req)
            }
        }
    }

    override fun toString(): String =
        """Account Name: $key
        """.trimMargin()
}