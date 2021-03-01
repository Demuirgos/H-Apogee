using Avalonia;
using Avalonia.Controls;
using Avalonia.Markup.Xaml;

namespace ClientSideComponants
{
    public class SideBar : UserControl
    {
        public SideBar()
        {
            InitializeComponent();
        }

        private void InitializeComponent()
        {
            AvaloniaXamlLoader.Load(this);
        }
    }
}