using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Text.RegularExpressions;

namespace GenieLogicielProject.Componants
{
    class FormData
    {
        public enum RequestType
        {
            Attestation,
            Bulletin
        }
        string _email;
        public string Email
        {
            get => _email; 
            set
            {
                if (Regex.Match(value, "[0-9A-Za-z.]+@[a-zA-Z]+[.][a-zA-Z]+").Success)
                {
                    _email = value;
                }
                else
                {
                    throw new Exception("Email Invalid");
                }
            }
        }

        public string FirstName
        { get; set; }

        public string LastName
        { get; set; }

        public long Id
        { get; set; }

        public string CIN
        { get; set; }

        public DateTime Date
        { get; set; }

        public RequestType Request
        { get; set; }
    }
}
