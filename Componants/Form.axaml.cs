using Avalonia;
using Avalonia.Controls;
using Avalonia.Markup.Xaml;
using Avalonia.Controls.ApplicationLifetimes;
using Avalonia.Interactivity;
using Google.Apis.Drive.v3.Data;
using MsgBox;
using System;
using System.IO;
using System.Linq;
using System.Text;
using ApogeeClient;

namespace ClientSideComponants
{
    public class Form : UserControl
    {
        FormData Model = new FormData();
        public Form()
        {
            InitializeComponent();
        }

        private void InitializeComponent()
        {
            AvaloniaXamlLoader.Load(this);
            this.DataContext = Model;
        }
        private void DemandeBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            Model.Request = (sender as ComboBox).SelectedIndex switch
            {
                1 => FormData.RequestType.Bulletin,
                _ => FormData.RequestType.Attestation,
            };
        }

        private async void Submit_Click(object sender, RoutedEventArgs e)
        {
            try{
                Model.Date = DateTime.Now;
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