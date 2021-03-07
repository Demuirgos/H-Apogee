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
        public static List<MessageModel> FetchEmails() {
            MessageModel Message = new MessageModel();
            var (host,user,password) = ("pop.gmail.com","Genie.Logiciel.Project@gmail.com","Genie12345");
            using (var client = new Pop3Client()){
                client.Connect(host,110,false);
                client.Authenticate(user,password);
                var Messages =  from message in from i in Enumerable.Range(0,client.GetMessageCount()) 
                                                select client.GetMessage(i)
                                let subject  = message.Headers.Subject.Trim()
                                let body = message.FindFirstPlainTextVersion() 
                                where subject.StartsWith("[AdminReply]") 
                                select new MessageModel {
                                    Subject = subject,
                                    Body    = body.GetBodyAsText(),
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

//"ApiCode":"AIzaSyDf6r84Nxbj5Fb0FowPqr9pfCEpKXpa_8k"