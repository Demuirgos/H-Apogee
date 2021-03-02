package adminSide

enum class Doc {
    ReleveDeNote,
    AttestationDeScolarite,
    AttestationDeStage,
    Default;

    companion object {
        fun parse(string: String) =
            when (string.toLowerCase()) {
                "releve de note" -> ReleveDeNote
                "0" -> ReleveDeNote
                "attestation de scolarite" -> AttestationDeScolarite
                "1" -> AttestationDeScolarite
                "attestation de stage" -> AttestationDeStage
                "2" -> AttestationDeStage
                else -> Default
            }
    }

    override fun toString(): String =
        when (this) {
            ReleveDeNote -> "Relevé de notes"
            AttestationDeScolarite -> "Attestation de scolarité"
            AttestationDeStage -> "Attestation de stage"
            Default -> ""
        }
}
