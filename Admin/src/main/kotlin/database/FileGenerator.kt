package database

import adminSide.Doc
import java.io.File
import java.io.FileOutputStream


object FileGenerator {
  fun createFile(docType: Doc) {
    val fos = FileOutputStream("files/temp/temp.tex")
    fos.write(
      when (docType) {
        Doc.ReleveDeNote -> releveMod()
        Doc.AttestationDeScolarite -> scolariteMod()
        Doc.AttestationDeStage -> stageMod()
        Doc.Default -> throw Error("type demande invalide")
      }.toByteArray()
    )
    fos.close()
  }


  private fun releveMod() =
    File("files/releveDeNote/ReleveDeNote.tex").readText()
  // TODO: 3/3/21

  private fun scolariteMod() =
    File("files/attestationDeScolarite/attestationDeScolarite.tex").readText()
   // TODO: 3/3/21

  private fun stageMod() =
    File("files/attestationDeStage/attestationDeStage.tex").readText()
   // TODO: 3/3/21
}