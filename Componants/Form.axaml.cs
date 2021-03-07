using Avalonia;
using Avalonia.Controls;
using Avalonia.Markup.Xaml;
using Avalonia.Controls.ApplicationLifetimes;
using Avalonia.Interactivity;

using MsgBox;
using System;
using System.IO;
using System.Text;
using System.ComponentModel;

using ApogeeClient;

namespace ClientSideComponants
{
    public class Form : UserControl
    {
        FormData _model = new FormData();
        public FormData Model {
            get => _model;
            set {
                _model = value;
                SetView();
                hookSubscriber();
            }  
        }

        public Form()
        {
            InitializeComponent();
        }

        private void InitializeComponent()
        {
            AvaloniaXamlLoader.Load(this);
            this.DataContext = Model;
        }

        private void SetView(){
            this.FindControl<TextBox>("FirstNameBox").Text = _model.FirstName;
            this.FindControl<TextBox>("LastNameBox").Text = _model.LastName;
            this.FindControl<TextBox>("CINBox").Text = _model.CIN;
            this.FindControl<TextBox>("ApogeeBox").Text = _model.Id;
            this.FindControl<TextBox>("EmailBox").Text = _model.Email;
            this.FindControl<TextBox>("CneBox").Text = _model.CNE;
            this.FindControl<ComboBox>("DemandeBox").SelectedIndex = _model.Request == FormData.RequestType.RelevéDeNotes?1: _model.Request ==  FormData.RequestType.AttestationDeScolarité?2:3;
        }

        private void hookSubscriber(){
            this.FindControl<TextBox>("FirstNameBox").PropertyChanged += (object src,Avalonia.AvaloniaPropertyChangedEventArgs args) => _model.FirstName = (src as TextBox).Text;
            this.FindControl<TextBox>("LastNameBox").PropertyChanged += (object src,Avalonia.AvaloniaPropertyChangedEventArgs args) => _model.LastName = (src as TextBox).Text;
            this.FindControl<TextBox>("CINBox").PropertyChanged += (object src,Avalonia.AvaloniaPropertyChangedEventArgs args) => _model.CIN = (src as TextBox).Text;
            this.FindControl<TextBox>("ApogeeBox").PropertyChanged += (object src,Avalonia.AvaloniaPropertyChangedEventArgs args) => _model.Id = (src as TextBox).Text;
            this.FindControl<TextBox>("EmailBox").PropertyChanged += (object src,Avalonia.AvaloniaPropertyChangedEventArgs args) => _model.Email = (src as TextBox).Text;
            this.FindControl<TextBox>("CneBox").PropertyChanged += (object src,Avalonia.AvaloniaPropertyChangedEventArgs args) => _model.CNE = (src as TextBox).Text;
        }

        private void DemandeBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            Model.Request = (sender as ComboBox).SelectedIndex switch
            {
                0 => FormData.RequestType.RelevéDeNotes ,
                1 => FormData.RequestType.AttestationDeScolarité,
                _ => FormData.RequestType.AttestationDeStage,
            };
        }

        private async void Submit_Click(object sender, RoutedEventArgs e)
        {
            try{
                Model.Date = DateTime.Now.ToString();
                var data = Model.Serialize();
                string FileName = $"{data.GetHashCode()}_{Model.GetHashCode()}_Request.json";
                using (var stream = new StreamWriter(FileName))
                {
                    stream.WriteLine(data);
                }
                var service = Authentification.Connect();
                var response = await Authentification.Upload(service, FileName);
                if (Avalonia.Application.Current.ApplicationLifetime is IClassicDesktopStyleApplicationLifetime desktop)
                {
                    await  MessageBox.Show(desktop.MainWindow, response , "Status", MessageBox.MessageBoxButtons.Ok);
                }
            } catch (Exception ex) {
                if (Avalonia.Application.Current.ApplicationLifetime is IClassicDesktopStyleApplicationLifetime desktop)
                {
                    await  MessageBox.Show(desktop.MainWindow, ex.Message , "Error", MessageBox.MessageBoxButtons.Ok);
                }
            }
        }

    }
}