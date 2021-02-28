using Avalonia;
using Avalonia.Controls;
using Avalonia.Markup.Xaml;
using Avalonia.Interactivity;
using MsgBox;
using System;
using System.IO;
using System.Linq;
using System.Text;

namespace ApogeeClient
{
    public class MainWindow : Window
    {
        FormData Model = new FormData();

        public MainWindow()
        {
            InitializeComponent();
        }

        private void InitializeComponent()
        {
            AvaloniaXamlLoader.Load(this);
            OAuth2.Connect();
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
                using (var stream = new StreamWriter("RequestDemo.txt"))
                {
                    stream.WriteLine(data);
                }
            } catch (Exception ex) {
                await  MessageBox.Show(this, ex.Message , "Error", MessageBox.MessageBoxButtons.Ok);
            }
        }
        
    }
}