
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
    public class PlateModel  : NotifierClass {

        public PlateModel(FormData data,int i){
            ModelRef = data;
            index = i;
            HookEvent() ;
        }

        public PlateModel(){
            ModelRef = new FormData();
            index = 0;
            HookEvent() ;
        }

            
        private void HookEvent() {
            ModelRef.PropertyChanged += (object src,PropertyChangedEventArgs args) => {
                ModelRef = src as FormData;
            };
        }

        FormData _ModelRef;
        public FormData ModelRef {
            get => _ModelRef;
            set {
                _ModelRef = value;
                NotifyPropertyChanged();
            }
        }
        public  int index {get; init;} = 0;
        public string Title => $"Request N° : {index}";
        public string Date => ModelRef.Date;
        public string Request => ModelRef.Request.ToString();

    }

}