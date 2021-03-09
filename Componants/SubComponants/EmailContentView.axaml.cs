using Avalonia;
using Avalonia.Controls;
using Avalonia.Markup.Xaml;
using ApogeeClient;

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
        }
    }
}