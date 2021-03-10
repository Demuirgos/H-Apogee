package paperWork

import java.io.File
import java.io.FileOutputStream

interface PaperWork {
    fun generateFile(id: Int) {
        val f = File("files/temp")
        f.mkdir()
        FileOutputStream("files/temp/temp.tex").write(getData(id).toByteArray())
        val com = Runtime.getRuntime().exec("/home/silverest/Coding/Admin/pdfGen.sh")
        while (!File("files/temp/temp.pdf").exists() || com.isAlive);
    }

    fun getData(id: Int) : String


    fun String.listRep(xs: List<String>, rs: List<String>) : String=
        if (xs.isEmpty() || rs.isEmpty())
            this
        else
            this.replaceFirst(xs.first(), rs.first()).listRep(xs.drop(1),rs.drop(1))
}