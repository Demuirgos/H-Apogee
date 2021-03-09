using System;
using System.IO;
using System.Text;
using System.Linq;
using System.Data;
using System.Net.Http;
using System.Threading;
using System.Threading.Tasks;
using System.Collections.Generic;

using OpenPop.Mime;
using OpenPop.Pop3;

using ApogeeClient;

namespace ApogeeClient
{
    public class EmailApi {
        public static bool ConnectEmail(UserEmail EmailModel){
            var ((host,ssl,port),user,password) = (EmailModel.StmpServer,EmailModel.Email,EmailModel.Password);
            var client = new Pop3Client();
            try {
                client.Connect(host,port,ssl);
                client.Authenticate(user,password);
            } catch {
                return false;
            }
            return true;
        }

        public static List<MessageModel> FetchEmails(UserEmail EmailModel) {
            var ((host,ssl,port),user,password) = (EmailModel.StmpServer,EmailModel.Email,EmailModel.Password);
            using (var client = new Pop3Client()){
                client.Connect(host,port,ssl);
                client.Authenticate(user,password);
                var Messages =  from message in from i in Enumerable.Range(1,client.GetMessageCount()) 
                                                select client.GetMessage(i)
                                let subject  = message.Headers.Subject.Trim()
                                where subject.StartsWith("[AdminReply]") 
                                select new MessageModel {
                                    Subject = subject.Substring(11),
                                    Attachments =  (from attachment in message.FindAllAttachments()
                                                    select new MessageModel.Attachment {
                                                        FileName = attachment.FileName,
                                                        ContentType = attachment.ContentType.MediaType,
                                                        Content = attachment.Body
                                                    }).ToList()
                                };
                return Messages.ToList();
            }
        }

    }
}