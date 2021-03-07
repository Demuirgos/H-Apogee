using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Text.RegularExpressions;

namespace ApogeeClient
{
    public class MessageModel : NotifierClass
    {
        public class Attachment
        {
            public string FileName { get; set; }
            public string ContentType { get; set; }
            public byte[] Content { get; set; }
        }
        public string Subject;
        public string Body;
        public List<Attachment> Attachments;
    }
}
