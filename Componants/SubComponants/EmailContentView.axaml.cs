using Avalonia;
using Avalonia.Controls;
using Avalonia.Interactivity;
using Avalonia.Markup.Xaml;
using ApogeeClient;
using Avalonia.Controls.ApplicationLifetimes;

namespace ClientSideComponants
{
    public class EmailContentView : UserControl
    {
        MessageModel _model = new MessageModel();
        MessageModel Model {
            get => _model;
            set {
                _model = value;
                setViewValue();
            }
        }
        public EmailContentView()
        {
            InitializeComponent();
        }

        public EmailContentView(MessageModel model)
        {
            InitializeComponent();
            Model = model; 
        }

        private void InitializeComponent()
        {
            AvaloniaXamlLoader.Load(this);
        }

        private void setViewValue(){
            this.FindControl<TextBlock>("SubjectBox").Text = Model.Subject;
            if(Model.State){
                this.FindControl<StackPanel>("DownloadButtonFail").IsVisible = false;
                this.FindControl<StackPanel>("DownloadButtonSucc").IsVisible = true;
            } else {
                this.FindControl<StackPanel>("DownloadButtonFail").IsVisible = true;
                this.FindControl<StackPanel>("DownloadButtonSucc").IsVisible = false;
            }
        }

        private async void download_Click(object sender, RoutedEventArgs args){
            SaveFileDialog  picker = new();
            picker.Filters.Add(new FileDialogFilter() { Name = "PDF", Extensions = { "Pdf" } });
            picker.InitialFileName = _model.Attachments[0].FileName;
            var result = await picker.ShowAsync((Avalonia.Application.Current.ApplicationLifetime as IClassicDesktopStyleApplicationLifetime).MainWindow);
            if(result is not null)
                EmailApi.DownloadInto(Model, result);
        }
    }
}