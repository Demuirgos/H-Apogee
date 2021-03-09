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
            get => _email;
            set {
                _email = value;
                NotifyPropertyChanged();
            }
        }
        public string Password {get;set;}
        public bool Success => true;

        public (string,bool,int) StmpServer   =>  _email.Substring( _email.IndexOf('@') + 1 ,
                                                            _email.LastIndexOf('.') - _email.IndexOf('@') - 1)
                                                        .ToLower()
                                                        switch {
                                                            "gmail"         => ("pop.gmail.com",true,995),
                                                            "etu.uae.ac" => ("pop.gmail.com",true,995),
                                                            "outlook"       => ("pop3.live.com",true,995),
                                                            "hotmail"       => ("pop3.live.com",true,995),
                                                            "yahou"         => ("pop.mail.yahoo.com",true,995),
                                                            _ => throw new Exception("Email Provider Unsuported")
                                                        };


    }
}
