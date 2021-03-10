using Avalonia;
using Avalonia.Controls;
using Avalonia.Interactivity;
using Avalonia.Markup.Xaml;
using System.ComponentModel;
using ApogeeClient;
using MsgBox;
using System;
using Avalonia.Controls.ApplicationLifetimes;

namespace ClientSideComponants
{
    public class EmailLoginPrompt : Window
    {

        public new event PropertyChangedEventHandler PropertyChanged;
        protected void NotifyPropertyChanged(string propertyName = "")
        {
            if (PropertyChanged != null)
            {
                PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
            }
        }

        public enum  State
        {
            LoggedIn,
            LoggedOut
        }
        
        UserEmail _model = new UserEmail();
        public UserEmail Model => _model;
        State state = State.LoggedOut;

        public State LogState {
            get => state;
            set {
                state = value;
                NotifyPropertyChanged("");
            }
        }

        public EmailLoginPrompt()
        {
            InitializeComponent();
        }

        private void InitializeComponent()
        {
            AvaloniaXamlLoader.Load(this);
            hookSubscriber();
        }

        private void hookSubscriber(){
            this.FindControl<TextBox>("PasswordBox").PropertyChanged += (object src,Avalonia.AvaloniaPropertyChangedEventArgs args) => _model.Password = (src as TextBox).Text;
            this.FindControl<TextBox>("EmailBox").PropertyChanged += (object src,Avalonia.AvaloniaPropertyChangedEventArgs args) => _model.Email = (src as TextBox).Text;
        }

        private async void LogInButton_Click(object sender, RoutedEventArgs e){
            try{
                LogState = EmailApi.ConnectEmail(_model) switch {
                    true => State.LoggedIn,
                    false=> State.LoggedOut
                };
                await  MessageBox.Show((Avalonia.Application.Current.ApplicationLifetime as IClassicDesktopStyleApplicationLifetime).MainWindow, LogState.ToString(), "State", MessageBox.MessageBoxButtons.Ok);
            } catch(Exception ex) {
                await  MessageBox.Show((Avalonia.Application.Current.ApplicationLifetime as IClassicDesktopStyleApplicationLifetime).MainWindow, "Invalid Email / Password" , "Error", MessageBox.MessageBoxButtons.Ok);
            }
        }

        public void logOut(){
            EmailApi.DisconnectEmail();
        }
    }
}