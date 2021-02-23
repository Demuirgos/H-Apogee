using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
namespace GenieLogicielProject.Componants
{
    /// <summary>
    /// Interaction logic for Form.xaml
    /// </summary>
    public partial class Form : Page
    {
        FormData Model = new FormData();
        public Form()
        {
            InitializeComponent();
        }

        private void FirstNameBox_TextChanged(object sender, TextChangedEventArgs e)
        {
            Model.FirstName = (sender as TextBox).Text;
        }

        private void DemandeBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            Model.Request = (sender as ComboBox).SelectedIndex switch
            {
                1 => FormData.RequestType.Bulletin,
                _ => FormData.RequestType.Attestation,
            };
        }

        private void LastNameBox_TextChanged(object sender, TextChangedEventArgs e)
        {
            Model.LastName = (sender as TextBox).Text;
        }

        private void CINBox_TextChanged(object sender, TextChangedEventArgs e)
        {
            Model.CIN = (sender as TextBox).Text;
        }

        private void ApogeeBox_TextChanged(object sender, TextChangedEventArgs e)
        {
            Model.Id = Convert.ToInt64((sender as TextBox).Text);
        }

        private void EmailBox_TextChanged(object sender, TextChangedEventArgs e)
        {
            Model.Email = (sender as TextBox).Text;
        }
    }
}
