using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Text.RegularExpressions;
using System.Text.Json;
using System.Text.Json.Serialization;
using System.ComponentModel;

namespace ApogeeClient
{
    public class UserEmail : NotifierClass
    {
        string _email = "";
        string _password = "";
        public string Email
        {
            get => _email;//Regex.Match(_email, "[0-9A-Za-z.]+@[a-zA-Z]+[.][a-zA-Z]+").Success?_email:throw new Exception("Email Invalid"); 
            set {
                _email = value;
                NotifyPropertyChanged();
            }
        }
        public bool Success => true;
    }
}
