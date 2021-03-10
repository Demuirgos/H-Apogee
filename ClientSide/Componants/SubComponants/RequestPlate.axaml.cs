using Avalonia;
using Avalonia.Controls;
using Avalonia.Markup.Xaml;
using ApogeeClient;
using System;
using System.ComponentModel;

namespace ClientSideComponants
{
    public class RequestPlate : UserControl
    {
        PlateModel Model = new();

        public RequestPlate()
        {
            InitializeComponent();
        }

        public RequestPlate(PlateModel modelRef)
        {
            this.Model = modelRef;
            InitializeComponent();
        }

        private void InitializeComponent()
        {
            AvaloniaXamlLoader.Load(this);
            this.DataContext = this.Model;
            setUIContext();
            SetEventHooks();
        }

        private void setUIContext(){
            var TitleBox  = this.FindControl<TextBlock>("TitleBox");
            TitleBox.Text = this.Model.Title;
            var TypeBox  = this.FindControl<TextBlock>("TypeBox");
            TypeBox.Text = this.Model.Request;
            var DateBox  = this.FindControl<TextBlock>("DateBox");
            DateBox.Text = this.Model.Date;
        }

        private void SetEventHooks(){
            this.Model.PropertyChanged += (object src, PropertyChangedEventArgs args) => {
                this.FindControl<TextBlock>("TypeBox").Text = this.Model.Request;
                this.FindControl<TextBlock>("DateBox").Text = this.Model.Date;
            };
        }
    }
}