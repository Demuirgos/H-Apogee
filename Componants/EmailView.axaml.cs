using Avalonia;
using Avalonia.Controls;
using Avalonia.Markup.Xaml;

namespace ClientSideComponants
{
    public class EmailView : UserControl
    {
        public EmailView()
        {
            InitializeComponent();
        }

        private void InitializeComponent()
        {
            AvaloniaXamlLoader.Load(this);
        }
    }
}