using Avalonia;
using Avalonia.Controls;
using Avalonia.Markup.Xaml;
using ApogeeClient;
using System;

namespace ClientSideComponants
{
    public class RequestPlate : UserControl
    {
        PlateModel Model;
        public RequestPlate()
        {
            InitializeComponent(); 
        }

        private RequestPlate(ref FormData modelRef,int _index)
        {
            InitializeComponent();
            this.Model = new(ref modelRef,_index);
        }

        private void InitializeComponent()
        {
            AvaloniaXamlLoader.Load(this);
        }
    }
}