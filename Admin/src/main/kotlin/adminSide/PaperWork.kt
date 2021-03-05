package adminSide

import java.io.File
import java.io.FileOutputStream

interface PaperWork {
    fun generateFile(id: Int) {
        val f = File("files/temp")
        f.mkdir()
        FileOutputStream("files/temp/temp.tex").write(getData(id).toByteArray())
        Runtime.getRuntime().exec("pdflatex -output-directory files/temp files/temp/temp.tex")
        while (!File("files/temp/temp.pdf").exists());
    }

    fun getData(id: Int) : String
}