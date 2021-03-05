package database

import adminSide.Doc
import adminSide.ReleveDeNote
import java.io.File
import java.io.FileOutputStream


object FileGenerator {
  fun createFile(docType: Doc, id: Int) {
      when (docType) {
        Doc.ReleveDeNote -> ReleveDeNote.generateFile(id)
        Doc.AttestationDeScolarite -> TODO()
        Doc.AttestationDeStage -> TODO()
        Doc.Default -> throw Error("type demande invalide")
      }
  }

}

