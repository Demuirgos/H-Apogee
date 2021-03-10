package paperWork

import utils.Either

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

        fun parseNiv(c: Char) =
            when (c) {
                '1' -> "1\\`ere"
                else -> "$c\\`eme"
            }

        fun parseFilliere(niveau: String) =
            when {
                niveau.matches(Regex("CP[1-2]")) -> "Cycle Pr\\'eparatoire"
                niveau.matches(Regex("GI[1-3]")) -> "G\\'enie Informatique"
                niveau.matches(Regex("GC[1-3]")) -> "G\\'enie Civil"
                niveau.matches(Regex("SCM[1-3]")) -> "Supply Chain Management"
                niveau.matches(Regex("GSTR[1-3]")) -> "G\\'enie des Syst\\`eme de T\\'el\\'ecommunications et R\\'seaux"
                niveau.matches(Regex("GM[1-3]")) -> "G\\'enie M\\'ecatronique"
                else -> throw Error("Filliere invalide")
            }

        fun parseDip(niveau: String) =
            when {
                niveau == "CP1" -> "Ann\\'ee Pr\\'eparatoire"
                niveau == "CP2" -> "Ann\\'ee Pr\\'eparatire au Cycle Ing\\'enieur"
                niveau.contains(Regex("GI|GC|GM|SCM|GSTR")) -> "Cycle Ing\\'enieur"
                else -> throw Error ("Niveau Invalide")
            }

    }

    override fun toString(): String {
        return when (this) {
            CP -> "Cycle pr\\'eparatoire"
            CI -> "Cycle ing\\'enieur"
        }
    }
}
