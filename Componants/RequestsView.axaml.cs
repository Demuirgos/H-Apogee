using Avalonia;
using Avalonia.Controls;
using Avalonia.Interactivity;
using Avalonia.Markup.Xaml;

using ApogeeClient;

using System.ComponentModel;

using System;
using System.IO;
using System.Linq;
using System.Text.Json;
using System.Threading.Tasks;
using System.Collections.Generic;
using System.Text.Json.Serialization;
using System.Collections.ObjectModel;
using System.Collections.Specialized;

using MsgBox;

using Avalonia.Controls.ApplicationLifetimes;

namespace ClientSideComponants
{
    public class RequestsView : UserControl
    {
        ObservableCollection<FormData> forms = new ObservableCollection<FormData>();
        Window holder => (Avalonia.Application.Current.ApplicationLifetime is IClassicDesktopStyleApplicationLifetime desktop)?desktop.MainWindow:null;
        int Index {
            get => this.FindControl<SideBar>("PlatesBar").Index;
            set {
                this.FindControl<SideBar>("PlatesBar").Index  = value;
                navigateTo(this.FindControl<SideBar>("PlatesBar").Index);
            }
        }

        public RequestsView()
        {
            InitializeComponent();
        }

        private void InitializeComponent()
        {
            AvaloniaXamlLoader.Load(this);
            this.FindControl<SideBar>("PlatesBar").PropertyChanged += (object src,PropertyChangedEventArgs args) => {
                if((src as SideBar).Index >= 0)
                    this.FindControl<ClientSideComponants.Form>("Board").Model = forms[(src as SideBar).Index];
            };
            AddForm();
            this.FindControl<SideBar>("PlatesBar").Index = 0;
        }

        public void AddForm(FormData added = null){
            forms.Add(added is null? new FormData() : added);
            this.FindControl<SideBar>("PlatesBar").AddPlate(forms[forms.Count - 1]);
        }

        public async void RemoveForm(){
            if(forms.Count > 1){
                if (Index < 0 || Index > forms.Count) {
                    await  MessageBox.Show(holder, "no request is selected" , "Error", MessageBox.MessageBoxButtons.Ok);
                } else {
                    forms.RemoveAt(Index);
                    this.FindControl<SideBar>("PlatesBar").RemoveAt(Index);
                    Index = Index >= forms.Count ? forms.Count - 1 : Index ;
                }
            } else {
                await  MessageBox.Show(holder, "Cannot delete anymore requests" , "Error", MessageBox.MessageBoxButtons.Ok);
            }
        }

        public void Clear(){
            while(forms.Count > 1){
                var idx = forms.Count - 1;
                forms.RemoveAt(idx);
                this.FindControl<SideBar>("PlatesBar").RemoveAt(idx);
            }

        }

        public void navigateTo(int newIdx) {
            if(!(newIdx >= forms.Count || newIdx < 0)){
                Index = newIdx;
                var FormBoard  = this.FindControl<ClientSideComponants.Form>("Board");
                FormBoard.Model = forms[Index];
            }
        }

        private void AddBtn_Click(object sender, RoutedEventArgs args){
            AddForm();
        }

        
        private void RemBtn_Click(object sender, RoutedEventArgs args){
            RemoveForm();
        }
        
        private void ClrBtn_Click(object sender, RoutedEventArgs args){
            Clear();
        }
        
        private void SaveForms(string path){
            var jsonFormat = JsonSerializer.Serialize(forms);
            using (var stream = new StreamWriter(path))
            {
                stream.WriteLine(jsonFormat);
            } 
        }

        private async void SavBtn_Click(object sender, RoutedEventArgs args){
            SaveFileDialog  picker = new();
            picker.Filters.Add(new FileDialogFilter() { Name = "Exjs", Extensions = { "exjs" } });
            picker.InitialFileName = "UserForms";
            var result = await picker.ShowAsync(holder);
            if(result is not null)
                SaveForms(result);
        }

        private void OpenForms(string path){
            string data;
            using (var stream = new StreamReader(path))
            {
                data = stream.ReadToEnd();
            } 
            var jsonData = JsonSerializer.Deserialize<List<FormData>>(data);
            foreach (var form in jsonData)
            {
                AddForm(form);
            }
        }

        private async void OpnBtn_Click(object sender, RoutedEventArgs args){
            OpenFileDialog  picker = new();
            picker.Filters.Add(new FileDialogFilter() { Name = "Exjs", Extensions = { "exjs" } });
            var result = await picker.ShowAsync(holder);
            if(result is not null)
                OpenForms(result[0]);
        }

        private void LogInBtn_Click(object sender, RoutedEventArgs args){
            EmailLoginPrompt test = new EmailLoginPrompt();
            test.Show();
        }
        
    }
}