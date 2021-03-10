using System;
using System.IO;
using System.Text;
using System.Linq;
using System.Data;
using System.Net.Http;
using System.Threading;
using System.Diagnostics;
using System.Threading.Tasks;
using System.Collections.Generic;

using OpenPop.Mime;
using OpenPop.Pop3;

using ApogeeClient;

namespace ApogeeClient
{

    public class EmailApi {
        private static Pop3Client session = new Pop3Client();
        public static bool ConnectEmail(UserEmail EmailModel){
            var ((host,ssl,port),user,password) = (EmailModel.StmpServer,EmailModel.Email,EmailModel.Password);
            try {
                session.Connect(host,port,ssl);
                session.Authenticate(user,password);
                return true;
            } catch {
                return false;
            }
        }

        public static void DisconnectEmail(){
            session.Dispose();
            session =  new Pop3Client();
        }

        public static int EmailCount() {
            try{
                return session.GetMessageCount();
            } catch {
                return 0;
            }
        } 

        public static List<MessageModel> FetchEmails(UserEmail EmailModel) {
                var Messages =  from message in from i in Enumerable.Range(1,session.GetMessageCount()) 
                                                select session.GetMessage(i)
                                let subject  = message.Headers.Subject.Trim()
                                where subject.StartsWith("[AdminAccept]") || subject.StartsWith("[AdminRefus]")
                                select new MessageModel {
                                    Subject = subject.Substring(13),
                                    Attachments =  (from attachment in message.FindAllAttachments()
                                                    select new MessageModel.Attachment {
                                                        FileName = attachment.FileName,
                                                        ContentType = attachment.ContentType.MediaType,
                                                        Content = attachment.Body
                                                    }).ToList(),
                                    State = subject.StartsWith("[AdminAccept]")
                                };
                return Messages.ToList();
        }

        public static void DownloadInto(MessageModel mail, string path){
            List<MessageModel.Attachment> attachments = mail.Attachments;
            MessageModel.Attachment attachment = attachments[0];
            using (var stream = new BinaryWriter(File.Open(path, FileMode.Create)))
            {
                stream.Write(attachment.Content);
            }
            System.Diagnostics.Process.Start(path);

        }
    }
}