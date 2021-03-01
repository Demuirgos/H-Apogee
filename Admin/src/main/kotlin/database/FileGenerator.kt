package database

import adminSide.Request
import java.io.File

object FileGenerator {
  fun createFile(docType: Request.Doc) =
    File("cred.json")

}