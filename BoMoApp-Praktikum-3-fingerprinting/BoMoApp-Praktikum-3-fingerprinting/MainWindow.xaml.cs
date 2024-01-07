using BoMoApp_Praktikum_3_fingerprinting.Models;
using Microsoft.Win32;
using System;
using System.Globalization;
using System.IO;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using static BoMoApp_Praktikum_3_fingerprinting.MainWindow;

namespace BoMoApp_Praktikum_3_fingerprinting
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {

        List<Trace> offlineTraces = new List<Trace>();


        List<Trace> onlineTraces = new List<Trace>();
  


        public MainWindow()
        {
            InitializeComponent();
        }



        private void CheckButtonClick(object sender, RoutedEventArgs e)
        {
            // Hole den eingegebenen Text aus dem TextBox
            if (inputDataTextBox.Text.Length == 0) return;

            string[] inputDataText = inputDataTextBox.Text.TrimEnd(';').Split(';');

            List<AccessPoint> inputData = inputDataText.Select(ParseInputAccessPoint).ToList();

            if (offlineTraces.Count == 0) return;
            NearestNeighborFinder finder = new NearestNeighborFinder(offlineTraces);


            Position nearestNeighborPosition = finder.FindNearestNeighbor(inputData);

            // Jetzt kannst du die gefundenen Koordinaten verwenden
            if (nearestNeighborPosition != null)
            {
                // Ausgabe der Koordinaten
                MessageBox.Show($"Nearest Neighbor: X={nearestNeighborPosition.X}, Y={nearestNeighborPosition.Y}");
            }
        }

        private void OpenOnlineFileDialogButton_Click(object sender, RoutedEventArgs e)
        {
            OpenFileDialog openFileDialog = new OpenFileDialog();
            openFileDialog.Filter = "All files (*.*)|*.*";

            if (openFileDialog.ShowDialog() == false)
                return;

            // Zeige den ausgewählten Dateipfad an
            onlineFilePath.Text = openFileDialog.FileName;
            onlineTraces.Clear();

            try
            {
                // Öffne die Datei zum Lesen
                using (StreamReader reader = new StreamReader(openFileDialog.FileName))
                {
                    string line;
                    while ((line = reader.ReadLine()) != null)
                    {
                        //// Parsen der Zeile und Erstellen eines Trace-Objekts
                        Trace trace = ParseTrace(line);

                        if (trace != null)
                        {
                            // Füge das Trace-Objekt zur Liste hinzu
                            onlineTraces.Add(trace);
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                System.Diagnostics.Debug.WriteLine($"Fehler beim Lesen der Datei: {ex.Message}");
            }

            // Gruppiere Traces nach Positionskoordinaten X und Y
            var groupedTraces = onlineTraces.GroupBy(trace => new { trace.Position.X, trace.Position.Y });

            //  traceDataGrid.ItemsSource = groupedTraces;
            var medianTraces = getMedianTraces(onlineTraces);
            traceDataGrid.ItemsSource = medianTraces;


        }



        private void OpenOfflineFileDialogButton_Click(object sender, RoutedEventArgs e)
        {
            OpenFileDialog openFileDialog = new OpenFileDialog();
            openFileDialog.Filter = "All files (*.*)|*.*";

            if (openFileDialog.ShowDialog() == false)
                return;

            offlineTraces.Clear();

            // Zeige den ausgewählten Dateipfad an
            offlineFilePath.Text = openFileDialog.FileName;

            try
            {
                // Öffne die Datei zum Lesen
                using (StreamReader reader = new StreamReader(openFileDialog.FileName))
                {
                    string line;
                    while ((line = reader.ReadLine()) != null)
                    {
                        //// Parsen der Zeile und Erstellen eines Trace-Objekts
                        Trace trace = ParseTrace(line);

                        if (trace != null)
                        {
                            // Füge das Trace-Objekt zur Liste hinzu
                            offlineTraces.Add(trace);
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                System.Diagnostics.Debug.WriteLine($"Fehler beim Lesen der Datei: {ex.Message}");
            }

            // Gruppiere Traces nach Positionskoordinaten X und Y
            var groupedTraces = offlineTraces.GroupBy(trace => new { trace.Position.X, trace.Position.Y });

            //  traceDataGrid.ItemsSource = groupedTraces;
           offlineTraces =  getMedianTraces(offlineTraces);
        //    traceDataGrid.ItemsSource = medianTraces;

        }


        private List<Trace> getMedianTraces(List<Trace> traces)
        {
            // Gruppiere Traces nach Positionskoordinaten X und Y
            var groupedTraces = traces.GroupBy(trace => new { trace.Position.X, trace.Position.Y });

            List<Trace> medianTraces = new List<Trace>();


            foreach (var group in groupedTraces)
            {

                Trace medianTrace = new Trace
                {
                    Position = new Position { X = group.Key.X, Y = group.Key.Y },
                    AccessPoints = new List<AccessPoint>()
                };


                foreach (var accessPoint in group.First().AccessPoints)
                {
                    // Hole alle Signalstärken pro Accesspoint
                    var signalStrengths = group.SelectMany(trace => trace.AccessPoints.Where(ap => ap.Id == accessPoint.Id).Select(ap => ap.SignalStrength));

                    // Berechne den Median für jede Signalstärke pro Access Point
                     int medianSignalStrength = CalculateMedian(signalStrengths);

                    // Berechne den Durchschnitt für jede Signalstärke pro Access Point
                   // int medianSignalStrength = CalculateAverage(signalStrengths);

                    medianTrace.AccessPoints.Add(new AccessPoint
                    {
                        Id = accessPoint.Id,
                        MedianSignalStrength = medianSignalStrength
                    });
                }
                medianTraces.Add(medianTrace);
            }
            return medianTraces;
        }

        static int CalculateMedian(IEnumerable<int> values)
        {
            if (values == null || !values.Any())
                return 0;

            var sortedValues = values.OrderBy(v => v).ToList();
            int middle = sortedValues.Count / 2;

            if (sortedValues.Count % 2 == 0)
                return (int)((sortedValues[middle - 1] + sortedValues[middle]) / 2.0);
            else
                return sortedValues[middle];
        }

        static int CalculateAverage(IEnumerable<int> values)
        {
            if (values == null || !values.Any())
                return 0;

            double sum = 0;
            int count = 0;

            foreach (var value in values)
            {
                sum += value;
                count++;
            }

            return (int)(sum / count);
        }


        private Trace ParseTrace(string line)
        {

            Trace trace = new Trace();

            string[] parts = line.Split(';');

            if (parts.Length > 1)
            {
                trace.Position = ParsePosition(parts[1].Split('=')[1]);

                // Parsen der Access Points und Signalstärken
                trace.AccessPoints = parts.Skip(3).Select(ParseAccessPoint).ToList();

                return trace;
            }

            return null; // Wenn das Parsen fehlschlägt
        }
        private Position ParsePosition(string positionString)
        {

            string[] coordinates = positionString.Split(',');
            double x = double.Parse(coordinates[0], CultureInfo.InvariantCulture);
            double y = double.Parse(coordinates[1], CultureInfo.InvariantCulture);
            double z = double.Parse(coordinates[2], CultureInfo.InvariantCulture);

            return new Position { X = x, Y = y, Z = z };
        }


        private AccessPoint ParseAccessPoint(string accessPointString)
        {

            string[] parts = accessPointString.Split('=');
            string id = parts[0];
            string[] signalStrengthAndInfo = parts[1].Split(',');

            int signalStrength = int.Parse(signalStrengthAndInfo[0]);
            double frequency = double.Parse(signalStrengthAndInfo[1]);
            int channel = int.Parse(signalStrengthAndInfo[2]);

            return new AccessPoint { Id = id, SignalStrength = signalStrength, Frequency = frequency, Channel = channel };
        }

        private AccessPoint ParseInputAccessPoint(string accessPointString)
        {

            string[] signalStrengthAndInfo = accessPointString.Split('=');
            string id = signalStrengthAndInfo[0];
            

            int signalStrength = int.Parse(signalStrengthAndInfo[1]);

            return new AccessPoint { Id = id, SignalStrength = signalStrength };
        }



        private void CopyMenuItem_Click(object sender, RoutedEventArgs e)
        {
            // Kopiere ausgewählte Zellen in die Zwischenablage
            var selectedCells = traceDataGrid.SelectedCells;


            if (selectedCells.Any())
            {
                StringBuilder clipboardContent = new StringBuilder();

                foreach (var cellInfo in selectedCells)
                {
                    if (cellInfo.IsValid)
                    {
                        var cellValue = cellInfo.Item;

                        if (cellValue is Trace trace)
                        {
                            // Hier gehen wir durch die AccessPoints und extrahieren den gewünschten Text
                            foreach (var accessPoint in trace.AccessPoints)
                            {
                                clipboardContent.Append($"{accessPoint.Id}={accessPoint.MedianSignalStrength};");
                            }
                        }
 
                    }
                }
                // Kopiere den Inhalt in die Zwischenablage
                Clipboard.SetText(clipboardContent .ToString());
            }
        }
    }

   

}