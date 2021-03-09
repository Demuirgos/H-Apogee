using Avalonia;
using System;
using Avalonia.Controls;
using Avalonia.Markup.Xaml;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Collections.Specialized;
using ApogeeClient;
using System.Linq;
using System.ComponentModel;

namespace ClientSideComponants
{
    public class SideBar : UserControl, INotifyPropertyChanged
    {
        ObservableCollection<PlateModel> plates = new ObservableCollection<PlateModel>();
        public new event PropertyChangedEventHandler PropertyChanged ;

        private void NotifyPropertyChanged(String propertyName = "")
        {
            if (PropertyChanged != null)
            {
                PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
            }
        }

        public SideBar()
        {
            InitializeComponent();
            plates.CollectionChanged += OnListChanged;
            this.FindControl<ListBox>("SideBarView").SelectionChanged += (object src,SelectionChangedEventArgs args) => NotifyPropertyChanged();
        }

        private void OnListChanged(object sender, NotifyCollectionChangedEventArgs args)
        {
            var platesBox  = this.FindControl<ListBox>("SideBarView");
            platesBox.Items = from plate in plates select new RequestPlate(plate);
        }

        private void InitializeComponent()
        {
            AvaloniaXamlLoader.Load(this);
        }

        public void AddPlate(FormData fd){
            plates.Add(new PlateModel(fd, plates.Count));
        }

        public void RemoveAt(int i){
            if(i>0 && i<plates.Count)
                plates.RemoveAt(i);
        }

        public int Index {
            get => this.FindControl<ListBox>("SideBarView").SelectedIndex;
            set => this.FindControl<ListBox>("SideBarView").SelectedIndex = value;
        } 

        public void setEmails(List<MessageModel> mails) {
            this.FindControl<ListBox>("Notifications").Items = from mail in mails select new EmailContentView(mail);
        }
    }
}