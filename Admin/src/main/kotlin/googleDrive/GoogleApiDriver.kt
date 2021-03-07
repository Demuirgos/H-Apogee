package googleDrive

import adminSide.Request
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import utils.Either
import java.io.*

object GoogleApiDriver {
    private const val applicationName = "Service Etudiant"
    private val jsonFactory = JacksonFactory.getDefaultInstance()
    private const val tokens = "tokens"
    private val scopes = listOf(DriveScopes.DRIVE_FILE, DriveScopes.DRIVE)
    private const val credentials = "/home/silverest/Coding/Admin/cred.json"

    @Throws(IOException::class)
    private fun NetHttpTransport.getCredentials(): Credential? {
        val `in`: InputStream = FileInputStream(credentials)
        val clientSecrets = GoogleClientSecrets.load(jsonFactory, InputStreamReader(`in`))

        val flow = GoogleAuthorizationCodeFlow.Builder(
            this, jsonFactory, clientSecrets, scopes
        )
            .setDataStoreFactory(FileDataStoreFactory(File(tokens)))
            .setAccessType("offline")
            .build()
        val receiver = LocalServerReceiver.Builder().setPort(8888).build()
        return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
    }

    private val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
    private val service = Drive.Builder(httpTransport, jsonFactory, httpTransport.getCredentials())
        .setApplicationName(applicationName)
        .build()

    private fun downloadFileToReq (fileId: String): Request {
        val outputStream = ByteArrayOutputStream()
        service.files().get(fileId)
            .executeMediaAndDownloadTo(outputStream)
        return when (val ret = Request.fromJson(outputStream.toString(), fileId)!!) {
                is Either.Right -> ret.value!!
                is Either.Left -> Request.empty()
            }
    }

    fun downloadFile (fileId: String): String {
        val outputStream = ByteArrayOutputStream()
        service.files().get(fileId)
            .executeMediaAndDownloadTo(outputStream)
        return outputStream.toString()
    }

    fun checkFiles(): List<Request> {

        val result = service.files().list()
            .setPageSize(9)
            .setFields("nextPageToken, files(id, name)")
            .execute()
        val files: List<com.google.api.services.drive.model.File> = result.files.filter{ file -> file.name.endsWith(".json") }.distinctBy { it.name }
        return if (files.isEmpty()) {
            println("No files found.")
            emptyList()
        } else {
            files.map{ downloadFileToReq(it.id) }.filter {it.requestId != ""}
        }
    }



    fun deleteFile(fileId: String) {
        service.files().delete(fileId).executeUnparsed()
    }
}