package database

import adminSide.Doc
import paperWork.AttestationDeScolarite
import paperWork.AttestationDeStage
import paperWork.ReleveDeNote


object FileGenerator {
  fun createFile(docType: Doc, id: Int) {
      when (docType) {
        Doc.ReleveDeNote -> ReleveDeNote.generateFile(id)
        Doc.AttestationDeScolarite -> AttestationDeScolarite.generateFile(id)
        Doc.AttestationDeStage -> AttestationDeStage.generateFile(id)
        Doc.Default -> throw Error("type demande invalide")
      }
  }

}

