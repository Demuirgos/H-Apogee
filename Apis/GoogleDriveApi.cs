using Google.Apis.Drive.v3;
using Google.Apis.Services;
using Google.Apis.Util.Store;
using Google.Apis.Auth.OAuth2;
using Google.Apis.Drive.v3.Data;

using System;
using System.IO;
using System.Text;
using System.Linq;
using System.Net.Http;
using System.Threading;
using System.Threading.Tasks;
using System.Collections.Generic;

namespace ApogeeClient
{
    public class Authentification {
        static string[] Scopes = { DriveService.Scope.Drive,  
                           DriveService.Scope.DriveAppdata,      
                           DriveService.Scope.DriveFile,   
                           DriveService.Scope.DriveMetadataReadonly, 
                           DriveService.Scope.DriveReadonly,      
                           DriveService.Scope.DriveScripts };
        private static DriveService Service {get; set;}
        public static DriveService  Connect()
        {
            UserCredential credential;

            using (var stream =
                new FileStream("credentials.json", FileMode.Open, FileAccess.Read))
            {
                string credPath = "token.json";
                credential = GoogleWebAuthorizationBroker.AuthorizeAsync(
                    GoogleClientSecrets.Load(stream).Secrets,
                    Scopes,
                    "user",
                    CancellationToken.None,
                    new FileDataStore(credPath, true)).Result;
            }

            // Create Drive API service.
            Service = new DriveService(new BaseClientService.Initializer()
            {
                HttpClientInitializer = credential,
                ApplicationName = "StorageRoom",
            });
            return Service;
        }

        public static async Task<String>  Upload(DriveService service, string _uploadFile, string _parent = "", string _descrp = "Uploaded with .NET!")  
        {
            var fileMetadata = new Google.Apis.Drive.v3.Data.File()
            {
                Name = _uploadFile
            };
            FilesResource.CreateMediaUpload request;
            using (var stream = new System.IO.FileStream(_uploadFile,
                                    System.IO.FileMode.Open))
            {
                request =  service.Files.Create(
                    fileMetadata, stream, "application/unknown");
                request.Fields = "id";
                await request.UploadAsync();
            }
            var file = request.ResponseBody;
            return "Request Pending";
        }
        
        private static string GetMimeType(string fileName)
        {
            string mimeType = "application/unknown";
            return mimeType;
        } 

        
    } 
}