using Google.Apis.Auth.OAuth2;
using Google.Apis.Drive.v3;
using Google.Apis.Drive.v3.Data;
using Google.Apis.Services;
using Google.Apis.Util.Store;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace ApogeeClient
{
    public class Authentification {
        static string[] Scopes = { DriveService.Scope.DriveReadonly };
        private static DriveService Service {get; set;}
        private static void Connect()
        {
            UserCredential credential;

            using (var stream =
                new FileStream("credentials.json", FileMode.Open, FileAccess.Read))
            {
                // The file token.json stores the user's access and refresh tokens, and is created
                // automatically when the authorization flow completes for the first time.
                string credPath = "token.json";
                credential = GoogleWebAuthorizationBroker.AuthorizeAsync(
                    GoogleClientSecrets.Load(stream).Secrets,
                    Scopes,
                    "user",
                    CancellationToken.None,
                    new FileDataStore(credPath, true)).Result;
                Console.WriteLine("Credential file saved to: " + credPath);
            }

            // Create Drive API service.
            Service = new DriveService(new BaseClientService.Initializer()
            {
                HttpClientInitializer = credential,
                ApplicationName = "StorageRoom",
            });

        }
        public static void Upload(string _uploadFile, string _descrp = "Uploaded with .NET!")  
        {      
            Connect();
            var _service = Service;
            if (System.IO.File.Exists(_uploadFile))  
            {  
                var body = new Google.Apis.Drive.v3.Data.File();
                body.Name = System.IO.Path.GetFileName(_uploadFile);
                body.MimeType = GetMimeType(_uploadFile);

                FilesResource.CreateMediaUpload request;
                using (var stream = new System.IO.FileStream(_uploadFile, System.IO.FileMode.Open))
                {
                    request = _service.Files.Create(body, stream, body.MimeType);
                    request.Fields = "id";
                    request.Upload();
                }
            }  
            else  
            {  
               throw new Exception("The file does not exist. 404");  
            }  
        }
        private static string GetMimeType(string fileName)
      {
          return "application/unknown";
      }
    } 
}

//"ApiCode":"AIzaSyDf6r84Nxbj5Fb0FowPqr9pfCEpKXpa_8k"