using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Text.RegularExpressions;
using System.Text.Json;
using System.Text.Json.Serialization;

namespace ApogeeClient
{
    public class FormData
    {
        public enum RequestType
        {
            Attestation,
            Bulletin
        }
        string _firstName;
        string _lastName;
        string _CIN;
        string _id;
        string _email;
        public string Email
        {
            get => Regex.Match(_email, "[0-9A-Za-z.]+@[a-zA-Z]+[.][a-zA-Z]+").Success?_email:throw new Exception("Email Invalid"); 
            set => _email = value;
        }

        public string FirstName
        {   
            get => String.IsNullOrWhiteSpace(_firstName)?throw new Exception("First Name missing"):_firstName; 
            set => _firstName = value;
        }

        public string LastName
        {   
            get => String.IsNullOrWhiteSpace(_lastName)?throw new Exception("Last Name missing"):_lastName; 
            set => _lastName = value;
        }

        public String Id
        {   
            get => String.IsNullOrWhiteSpace(_id)?throw new Exception("ID Invalid"):_id; 
            set => _id = value;
        }

        public string CIN
        {   
            get => String.IsNullOrWhiteSpace(_CIN)?throw new Exception("CIN Invalid"):_CIN; 
            set => _CIN = value;
        }

        public DateTime Date
        { get; set; }

        public RequestType Request
        { get; set; }

        public String Serialize() => JsonSerializer.Serialize(this);
    }
}
