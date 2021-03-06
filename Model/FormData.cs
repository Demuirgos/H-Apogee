﻿using System;
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
    public class FormData : NotifierClass
    {
        public enum RequestType
        {
            Attestation,
            Bulletin
        }

        string _firstName = "";
        string _lastName  = "";
        string _CIN       = "";
        string _id        = "";
        string _email     = "";
        DateTime _date    = DateTime.Now;
        RequestType _type = RequestType.Attestation;
        public string Email
        {
            get => _email;//Regex.Match(_email, "[0-9A-Za-z.]+@[a-zA-Z]+[.][a-zA-Z]+").Success?_email:throw new Exception("Email Invalid"); 
            set {
                _email = value;
                NotifyPropertyChanged();
            }
        }

        public string FirstName
        {   
            get => _firstName;//String.IsNullOrWhiteSpace(_firstName)?throw new Exception("First Name missing"):_firstName; 
            set {
                _firstName = value;
                NotifyPropertyChanged();
            }
        }

        public string LastName
        {   
            get => _lastName;//String.IsNullOrWhiteSpace(_lastName)?throw new Exception("Last Name missing"):_lastName; 
            set {
                _lastName = value;
                NotifyPropertyChanged();
            }
        }

        public String Id
        {   
            get => _id;//String.IsNullOrWhiteSpace(_id)?throw new Exception("ID Invalid"):_id; 
            set {
                _id = value;
                NotifyPropertyChanged();
            }
        }

        public string CIN
        {   
            get => _CIN;//String.IsNullOrWhiteSpace(_CIN)?throw new Exception("CIN Invalid"):_CIN; 
            set {
                _CIN = value;
                NotifyPropertyChanged();
            }
        }

        public DateTime Date
        { 
            get => _date;
            set {
                _date = value;
                NotifyPropertyChanged();
            }
        }

        public string Request
        { 
            get => _type.ToString();
            set {
                _type = value=="Bulletin"?RequestType.Bulletin:RequestType.Attestation;
                NotifyPropertyChanged();
            }
        }

        private bool Valid =>
            Regex.Match(_email, "[0-9A-Za-z.]+@[a-zA-Z]+[.][a-zA-Z]+").Success
            && !String.IsNullOrWhiteSpace(_id)
            && !String.IsNullOrWhiteSpace(_lastName)
            && !String.IsNullOrWhiteSpace(_firstName)
            && !String.IsNullOrWhiteSpace(_CIN) ;
            
        public String Serialize() => Valid ? JsonSerializer.Serialize(this) : throw new Exception("Form Invalid or incomplete");
    }
}
