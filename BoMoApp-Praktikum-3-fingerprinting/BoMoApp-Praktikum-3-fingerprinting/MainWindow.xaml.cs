using BoMoApp_Praktikum_3_fingerprinting.Models;
using Microsoft.Win32;
using System;
using System.Globalization;
using System.IO;
using System.Linq;
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
        List<List<Trace>> onlineTraces = new List<List<Trace>>();
        List<AccessPoint> KnownAPs = new List<AccessPoint>();

        public int K { get; set; }

        public MainWindow()
        {
            InitializeComponent();
            K = 1;
            DataContext = this;
            KnownAPs.Add(new AccessPoint { Id = "00:14:BF:B1:7C:54", SignalStrength = 0, MedianSignalStrength = 0 });
            KnownAPs.Add(new AccessPoint { Id = "00:16:B6:B7:5D:8F", SignalStrength = 0, MedianSignalStrength = 0 });
            KnownAPs.Add(new AccessPoint { Id = "00:14:BF:B1:7C:57", SignalStrength = 0, MedianSignalStrength = 0 });
            KnownAPs.Add(new AccessPoint { Id = "00:14:BF:B1:97:8D", SignalStrength = 0, MedianSignalStrength = 0 });
            KnownAPs.Add(new AccessPoint { Id = "00:16:B6:B7:5D:9B", SignalStrength = 0, MedianSignalStrength = 0 });
            KnownAPs.Add(new AccessPoint { Id = "00:14:6C:62:CA:A4", SignalStrength = 0, MedianSignalStrength = 0 });
            KnownAPs.Add(new AccessPoint { Id = "00:14:BF:3B:C7:C6", SignalStrength = 0, MedianSignalStrength = 0 });
            KnownAPs.Add(new AccessPoint { Id = "00:14:BF:B1:97:8A", SignalStrength = 0, MedianSignalStrength = 0 });
            KnownAPs.Add(new AccessPoint { Id = "00:14:BF:B1:97:81", SignalStrength = 0, MedianSignalStrength = 0 });
            KnownAPs.Add(new AccessPoint { Id = "00:16:B6:B7:5D:8C", SignalStrength = 0, MedianSignalStrength = 0 });
            KnownAPs.Add(new AccessPoint { Id = "00:11:88:28:5E:E0", SignalStrength = 0, MedianSignalStrength = 0 });


        }
        private void OpenFileDialog(object sender, RoutedEventArgs e)
        {
            Button button = (Button)sender;
            OpenFileDialog openFileDialog = new OpenFileDialog();
            openFileDialog.Filter = "All files (*.*)|*.*";
            openFileDialog.Multiselect = true;

            if (openFileDialog.ShowDialog() == false)
                return;

            switch (button.Tag)
            {
                case "offlineEmpiricalNN":
                    readAndParseOfflineFileEmpiricalNN(openFileDialog);
                    break;
                case "onlineEmpiricalNN":
                    readAndParseOnlineFile(openFileDialog);
                    break;
                case "offlineModelNN":
                    readAndParseOfflineFileModelNN(openFileDialog);
                    break;
                case "onlineModelNN":
                    readAndParseOnlineFile(openFileDialog);
                    break;

                default:
                    break;
            }
        }




        private void readAndParseOfflineFileEmpiricalNN(OpenFileDialog openFileDialog)
        {
            // Zeige den ausgewählten Dateipfad an
            offlineFilePathEmpiricalNN.Text = openFileDialog.FileName;
            offlineTraces.Clear();

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
            offlineTraces = getMedianTraces(offlineTraces);
        }

        private void readAndParseOfflineFileModelNN(OpenFileDialog openFileDialog)
        {
            // Zeige den ausgewählten Dateipfad an
            offlineFilePathModelNN.Text = openFileDialog.FileName;
            offlineTraces.Clear();

            try
            {
                // Öffne die Datei zum Lesen
                using (StreamReader reader = new StreamReader(openFileDialog.FileName))
                {
                    string line;

                    while ((line = reader.ReadLine()) != null)
                    {
                        //// Parsen der Zeile und Erstellen eines Trace-Objekts
                        Trace trace = ParseTraceFromModel(line);

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

        }

        private void readAndParseOnlineFile(OpenFileDialog openFileDialog)
        {
            // Zeige den ausgewählten Dateipfad an
            onlineFilePathEmpiricalNN.Text = openFileDialog.FileName;
            onlineFilePathModelNN.Text = openFileDialog.FileName;
            onlineTraces.Clear();


            for (int i = 0; i < openFileDialog.FileNames.Length; i++)
            {
                List<Trace> temp = new List<Trace>();
                try
                {
                    // Öffne die Datei zum Lesen
                    using (StreamReader reader = new StreamReader(openFileDialog.FileNames[i]))
                    {

                        string line;

                        while ((line = reader.ReadLine()) != null)
                        {

                            Trace trace = new Trace();

                            if (line[0] == '(')
                            {
                                trace = ParseTraceFromJavaGen(line);

                            }
                            else
                            {
                                // Parsen der Zeile und Erstellen eines Trace-Objekts
                                trace = ParseTrace(line);
                            }
                            if (trace != null)
                            {
                                // Füge das Trace-Objekt zur Liste hinzu
                                temp.Add(trace);

                            }

                        }
                    }
                    onlineTraces.Add(new List<Trace>(temp));
                }
                catch (Exception ex)
                {
                    System.Diagnostics.Debug.WriteLine($"Fehler beim Lesen der Datei: {ex.Message}");
                }

                var medianTraces = getMedianTraces(onlineTraces[i]);

                onlineTraces[i] = medianTraces;
            }
        }


        private void SaveResult(object sender, RoutedEventArgs e)
        {

            Button button = (Button)sender;

            if (button.Tag.ToString() == "saveResult")
            {
                List<List<ResultEntry>> resultTables = new List<List<ResultEntry>>();

                // Speicherpfad für die Textdatei
                string filePath = "Ergebnisse.txt";

                for (int i = 0; i < onlineTraces.Count; i++)
                {
                    List<ResultEntry> temp = new List<ResultEntry>();

                    foreach (var trace in onlineTraces[i])
                    {
                        List<AccessPoint> onlineAPs = trace.AccessPoints;
                        Position kNearestNeighborPosition = FindNearestNeighbors(onlineAPs);
                        if (kNearestNeighborPosition != null)
                        {
                            double distance = CalculateEuclideanDistancePosition(trace.Position, kNearestNeighborPosition);


                            // Speichern der Ergebnisse in der Tabelle
                            temp.Add(new ResultEntry
                            {
                                OnlinePosition = trace.Position,
                                OfflinePosition = kNearestNeighborPosition,
                                EuclideanDistance = distance
                            });
                        }
                    }

                    resultTables.Add(new List<ResultEntry>(temp));
                }


                List<ResultEntry> medianResultTable = new List<ResultEntry>();


                for (int i = 0; i < resultTables[0].Count; i++)
                {

                    List<ResultEntry> entry = new List<ResultEntry>();

                    for (int j = 0; j < resultTables.Count; j++)
                    {
                        entry.Add(new ResultEntry
                        {
                            EuclideanDistance = resultTables[j][i].EuclideanDistance,
                            OfflinePosition = resultTables[j][i].OfflinePosition,
                            OnlinePosition = resultTables[j][i].OnlinePosition
                        });
                    }
                    var MedianEuclideanDistance = CalculateDoubleMedian(entry.Select(e => e.EuclideanDistance));
                    var MedianOfflinePositionX = CalculateDoubleMedian(entry.Select(e => e.OfflinePosition.X));
                    var MedianOfflinePositionY = CalculateDoubleMedian(entry.Select(e => e.OfflinePosition.Y));
                    var MedianOnlinePositionX = CalculateDoubleMedian(entry.Select(e => e.OnlinePosition.X));
                    var MedianOnlinePositionY = CalculateDoubleMedian(entry.Select(e => e.OnlinePosition.Y));

                    medianResultTable.Add(new ResultEntry
                    {
                        EuclideanDistance = MedianEuclideanDistance,
                        OfflinePosition = new Position
                        {
                            X = MedianOfflinePositionX,
                            Y = MedianOfflinePositionY,
                            Z = 0
                        },
                        OnlinePosition = new Position
                        {
                            X = MedianOnlinePositionX,
                            Y = MedianOnlinePositionY,
                            Z = 0
                        }
                    });


                    using (StreamWriter writer = new StreamWriter(filePath))
                    {
                        foreach (var resultEntry in medianResultTable)
                        {
                            double roundedDistance = Math.Round(resultEntry.EuclideanDistance, 3);

                            if (errorCheckBoxTab1.IsChecked == true || errorCheckBoxTab2.IsChecked == true)
                            {
                                writer.WriteLine($"{roundedDistance.ToString("0.000")}");
                            }
                            else
                            {
                                writer.WriteLine($"euclDist={roundedDistance.ToString("0.000", CultureInfo.InvariantCulture)};" +
                                 $"onlinePos={resultEntry.OnlinePosition.X.ToString("00.00", CultureInfo.InvariantCulture)},{resultEntry.OnlinePosition.Y.ToString("00.00", CultureInfo.InvariantCulture)};" +
                               $"estimatedPos={resultEntry.OfflinePosition.X.ToString("00.00", CultureInfo.InvariantCulture)},{resultEntry.OfflinePosition.Y.ToString("00.00", CultureInfo.InvariantCulture)}");
                            }



                        }
                    }
                }
            }

            //  Speichere automatisch k1 bis k8
            else

            {
                for (int l = 1; l <= 8; l++)
                {
                    K = l;

                    List<List<ResultEntry>> resultTables = new List<List<ResultEntry>>();

                    // Speicherpfad für die Textdatei
                    string filePath = $"Ergebnisse k{K}.txt";

                    for (int i = 0; i < onlineTraces.Count; i++)
                    {
                        List<ResultEntry> temp = new List<ResultEntry>();

                        foreach (var trace in onlineTraces[i])
                        {
                            List<AccessPoint> onlineAPs = trace.AccessPoints;
                            Position kNearestNeighborPosition = FindNearestNeighbors(onlineAPs);
                            if (kNearestNeighborPosition != null)
                            {
                                double distance = CalculateEuclideanDistancePosition(trace.Position, kNearestNeighborPosition);


                                // Speichern der Ergebnisse in der Tabelle
                                temp.Add(new ResultEntry
                                {
                                    OnlinePosition = trace.Position,
                                    OfflinePosition = kNearestNeighborPosition,
                                    EuclideanDistance = distance
                                });
                            }
                        }

                        resultTables.Add(new List<ResultEntry>(temp));
                    }


                    List<ResultEntry> medianResultTable = new List<ResultEntry>();


                    for (int i = 0; i < resultTables[0].Count; i++)
                    {

                        List<ResultEntry> entry = new List<ResultEntry>();

                        for (int j = 0; j < resultTables.Count; j++)
                        {
                            entry.Add(new ResultEntry
                            {
                                EuclideanDistance = resultTables[j][i].EuclideanDistance,
                                OfflinePosition = resultTables[j][i].OfflinePosition,
                                OnlinePosition = resultTables[j][i].OnlinePosition
                            });
                        }
                        var MedianEuclideanDistance = CalculateDoubleMedian(entry.Select(e => e.EuclideanDistance));
                        var MedianOfflinePositionX = CalculateDoubleMedian(entry.Select(e => e.OfflinePosition.X));
                        var MedianOfflinePositionY = CalculateDoubleMedian(entry.Select(e => e.OfflinePosition.Y));
                        var MedianOnlinePositionX = CalculateDoubleMedian(entry.Select(e => e.OnlinePosition.X));
                        var MedianOnlinePositionY = CalculateDoubleMedian(entry.Select(e => e.OnlinePosition.Y));

                        medianResultTable.Add(new ResultEntry
                        {
                            EuclideanDistance = MedianEuclideanDistance,
                            OfflinePosition = new Position
                            {
                                X = MedianOfflinePositionX,
                                Y = MedianOfflinePositionY,
                                Z = 0
                            },
                            OnlinePosition = new Position
                            {
                                X = MedianOnlinePositionX,
                                Y = MedianOnlinePositionY,
                                Z = 0
                            }
                        });


                        using (StreamWriter writer = new StreamWriter(filePath))
                        {
                            foreach (var resultEntry in medianResultTable)
                            {
                                double roundedDistance = Math.Round(resultEntry.EuclideanDistance, 3);

                                if (errorCheckBoxTab1.IsChecked == true || errorCheckBoxTab2.IsChecked == true)
                                {
                                    writer.WriteLine($"{roundedDistance.ToString("0.000")}");
                                }
                                else
                                {
                                    writer.WriteLine($"euclDist={roundedDistance.ToString("0.000", CultureInfo.InvariantCulture)};" +
                                     $"onlinePos={resultEntry.OnlinePosition.X.ToString("00.00", CultureInfo.InvariantCulture)},{resultEntry.OnlinePosition.Y.ToString("00.00", CultureInfo.InvariantCulture)};" +
                                   $"estimatedPos={resultEntry.OfflinePosition.X.ToString("00.00", CultureInfo.InvariantCulture)},{resultEntry.OfflinePosition.Y.ToString("00.00", CultureInfo.InvariantCulture)}");
                                }



                            }
                        }
                    }
                }
                K = 1;


            }
        }



        public Position FindNearestNeighbors(List<AccessPoint> onlineAPs)
        {
            List<(double MinDistance, Position NearestNeighborPosition)> distanceAndPosition = new List<(double, Position)>();

            Position avgPosition = new Position(); // Initialisiere eine Position für den Durchschnitt

            foreach (var offlineTrace in offlineTraces)
            {
                double distance = CalculateEuclideanDistance(onlineAPs, offlineTrace.AccessPoints);
                distanceAndPosition.Add((distance, offlineTrace.Position));
            }

            // Es sollten mindestens soviele Treffer in der Liste sein, wie nach Nachbarn gesucht werden:
            if (distanceAndPosition.Count >= K)
            {

                // Sortiere aufsteigend nach minimaler Distanz zuerst
                var sortedList = distanceAndPosition.OrderBy(item => item.MinDistance).ToList();


                // k = 1: optionaler Parameter nimmt standardmäßig 1, wenn kein k angegeben wird

                for (int i = 0; i < K; i++)
                {
                    avgPosition.X += sortedList[i].NearestNeighborPosition.X;
                    avgPosition.Y += sortedList[i].NearestNeighborPosition.Y;
                }

                // Teile durch die Anzahl von Elementen
                avgPosition.X /= K;
                avgPosition.Y /= K;

                return avgPosition;

            }

            else
            {
                return null;
            }

        }


        private double CalculateEuclideanDistance(List<AccessPoint> onlineAPs, List<AccessPoint> offlineAPs)
        {
            double distanceSquared = 0;

            foreach (var onlineAP in onlineAPs)
            {
                var AP = offlineAPs.FirstOrDefault(offlineAP => offlineAP.Id == onlineAP.Id);
                if (AP == null)
                {
                    AP = new AccessPoint { MedianSignalStrength = 0 };

                }
                int signalStrengthDiff = onlineAP.MedianSignalStrength - AP.MedianSignalStrength;

                distanceSquared += signalStrengthDiff * signalStrengthDiff;
            }

            var result = Math.Sqrt(distanceSquared);

            return result;

        }

        static double CalculateEuclideanDistancePosition(Position pos1, Position pos2)
        {
            return Math.Sqrt(Math.Pow(pos1.X - pos2.X, 2) + Math.Pow(pos1.Y - pos2.Y, 2));
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
                    int medianSignalStrength = CalculateIntMedian(signalStrengths);

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

        static int CalculateIntMedian(IEnumerable<int> values)
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

        static double CalculateDoubleMedian(IEnumerable<double> values)
        {
            var sortedValues = values.OrderBy(x => x).ToList();
            int count = sortedValues.Count;

            if (count % 2 == 0)
            {
                // Wenn die Anzahl der Elemente gerade ist, nimm den Durchschnitt der beiden mittleren Werte
                int middleIndex1 = count / 2 - 1;
                int middleIndex2 = count / 2;
                return (sortedValues[middleIndex1] + sortedValues[middleIndex2]) / 2.0;
            }
            else
            {
                // Wenn die Anzahl der Elemente ungerade ist, nimm einfach den mittleren Wert
                int middleIndex = count / 2;
                return sortedValues[middleIndex];
            }
        }



        private Trace ParseTraceFromModel(string line)
        {

            string[] parts = line.Split(", ");

            if (parts.Length > 1)
            {

                double x = double.Parse(parts[0], CultureInfo.InvariantCulture);
                double y = double.Parse(parts[1], CultureInfo.InvariantCulture);
                double z = double.Parse(parts[2], CultureInfo.InvariantCulture);

                List<AccessPoint> accessPoints = new List<AccessPoint>();
                for (int i = 3; i < parts.Length - 1; i += 3)
                {
                    string id = parts[i];
                    double signalStrength = double.Parse(parts[i + 2], CultureInfo.InvariantCulture);

                    // Nur APs mit besserer Signalstärke als -90 werden hinzugefügt. Alle anderen sind "unhörbar"
                    if (signalStrength >= -90.0)
                    {
                        // Erstelle AccessPoint-Objekt und füge es zur Liste hinzu
                        accessPoints.Add(new AccessPoint { Id = id, MedianSignalStrength = (int)signalStrength });
                    }



                }

                return new Trace { Position = new Position { X = x, Y = y, Z = z }, AccessPoints = accessPoints };

            }

            return null; // Wenn das Parsen fehlschlägt
        }

        private Trace ParseTraceFromJavaGen(string line)
        {
            Trace trace = new Trace();

            string[] parts = line.Split(" - ;");
            if (parts.Length > 0)
            {
                // Erste Klammer am Anfang entfernen
                var positionPart = parts[0].Substring(1);
                positionPart = positionPart.Substring(0, positionPart.LastIndexOf(","));

                var xyz = positionPart.Split(",");
                double x = double.Parse(xyz[0], CultureInfo.InvariantCulture);
                double y = double.Parse(xyz[1], CultureInfo.InvariantCulture);
                double z = double.Parse(xyz[2], CultureInfo.InvariantCulture);

                List<AccessPoint> accessPoints = new List<AccessPoint>();

                var accessPointsPart = parts[1].Split(";");
                foreach (var apPart in accessPointsPart)
                {

                    string[] APparts = apPart.Split('=');
                    string id = APparts[0];
                    double signalStrength = double.Parse(APparts[1].Split(",")[0], CultureInfo.InvariantCulture);

                    bool idExists = KnownAPs.Any(ap => ap.Id == id);

                    // Nur APs mit besserer Signalstärke als -90 werden hinzugefügt. Alle anderen sind "unhörbar"
                    if (idExists && signalStrength >= -90.0)
                    {
                        accessPoints.Add(new AccessPoint { Id = id, SignalStrength = (int)signalStrength });
                    }
                }
                trace.Position = new Position { X = x, Y = y, Z = z };
                trace.AccessPoints = accessPoints;
            }

            return trace;
        }


        private Trace ParseTrace(string line)
        {

            Trace trace = new Trace();

            string[] parts = line.Split(';');

            if (parts.Length > 1)
            {

                // Skip Timestamp, Position and DeviceId
                var accessPointStrings = parts.Skip(3);

                trace.AccessPoints = accessPointStrings.Select(ParseAccessPoint).ToList();
                trace.AccessPoints.RemoveAll(ap => ap == null);
                trace.Position = ParsePosition(parts[1].Split('=')[1]);

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

            bool idExists = KnownAPs.Any(ap => ap.Id == id);

            // Nur APs mit besserer Signalstärke als -90 werden hinzugefügt. Alle anderen sind "unhörbar"
            if (idExists && signalStrength >= -90.0)
            {
                return new AccessPoint { Id = id, SignalStrength = signalStrength };
            }

            return null;
        }
    }

}