package database

import database.DatabaseApi.Admins.account
import database.DatabaseApi.Admins.adminId
import database.DatabaseApi.Admins.seed
import database.DatabaseApi.adminDB
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import utils.Either
import utils.SHA256.encrypt
import java.io.File
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

object SeedPhrase {
    private  val file = File("files/seeds").readLines().toTypedArray()

    fun generateSeed (): List<String> =
        IntRange(0, 5).map {
            file[nextInt(file.size)]
        }

    fun checkSeedPhrase (nomCompte: String, seedPhrase: String) {
        transaction (adminDB) {
            val fetch = DatabaseApi.Admins.select {
                account eq nomCompte
                seed eq seedPhrase.encrypt()
            }.firstOrNull()

            if (fetch != null) {
                val genPw = generateRandomPw()
                EmailSender.sendFile(
                    fetch[Expression.build { DatabaseApi.Admins.email }],
                    "Recuperation du compte",
                    "Votre nouveau mot de passe est $genPw",
                    null
                )

                DatabaseApi.Admins.update({adminId eq fetch[adminId]}) {
                    it[password] = genPw.encrypt()
                }

                print("done")
            }
        }
    }

    fun isRight(idCompte:String, seedPhrase: String) =
        transaction (adminDB) {
            return@transaction DatabaseApi.Admins.select {
                adminId eq idCompte
                seed eq seedPhrase.encrypt()
            }.firstOrNull() != null
        }

    fun generateNewSeed(idCompte:String, seedPhrase: String) =
             if (isRight(idCompte, seedPhrase)) {
                Either.Right(generateSeed())
            } else {
                Either.Left(Error("Data not found in DB"))
            }



    private fun generateRandomPw() : String {
        val chars = "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*?".toList()
        return IntRange(0, nextInt(10,16)).map {chars[nextInt(chars.size)]}.joinToString("")
    }

    @JvmStatic
    fun main (args: Array<String>) {
       checkSeedPhrase("root", "wave coconut reform renew festival chef")
    }
}

