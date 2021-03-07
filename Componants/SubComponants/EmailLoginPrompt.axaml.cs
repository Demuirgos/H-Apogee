using Avalonia;
using Avalonia.Controls;
using Avalonia.Markup.Xaml;

namespace ClientSideComponants
{
    public class EmailLoginPrompt : Window
    {
        public EmailLoginPrompt()
        {
            InitializeComponent();
        }

        private void InitializeComponent()
        {
            AvaloniaXamlLoader.Load(this);
        }
    }
}