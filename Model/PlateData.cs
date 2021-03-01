
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

public class PlateModel {
            public PlateModel(ref FormData data,int i){
                ModelRef = data;
                index = i;
            }
            public FormData ModelRef {get;set;}
            public  int index {get; init;} = 0;
            public string Title => $"Request N° : {index}";
            public string Date => ModelRef.Date.ToString();
            public string Request => ModelRef.Request.ToString();
        }

}