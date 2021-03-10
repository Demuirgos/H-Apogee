package database

import adminSide.Admin
import database.DatabaseApi.Admins.account
import database.DatabaseApi.Admins.adminId
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object LogSession {
    private lateinit var session: Admin

    fun startSession(adminSession: Admin) {
        session = adminSession
    }

    fun disconnect() {
        session = Admin.empty()
    }

    fun get(): Admin =
        session

    fun getKey(): String =
        session.getId()

    fun getAdminName() =
        transaction(DatabaseApi.adminDB) {
            return@transaction DatabaseApi.Admins.select {
                adminId eq getKey()
            }.firstOrNull()?.get(Expression.build { account })
        }
}