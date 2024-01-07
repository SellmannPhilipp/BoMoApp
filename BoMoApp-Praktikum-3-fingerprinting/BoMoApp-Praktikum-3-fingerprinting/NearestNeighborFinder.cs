using BoMoApp_Praktikum_3_fingerprinting.Models;


namespace BoMoApp_Praktikum_3_fingerprinting
{
    public class NearestNeighborFinder
    {

        private List<Trace> traces;

        public NearestNeighborFinder(List<Trace> traces)
        {
            this.traces = traces;
        }

        public Position FindNearestNeighbor(List<AccessPoint> inputSignalStrengths)
        {
            Position nearestNeighborPosition = null;
            double minDistance = double.MaxValue;

            foreach (var trace in traces)
            {
                double distance = CalculateEuclideanDistance(inputSignalStrengths, trace.AccessPoints);

                if (distance < minDistance)
                {
                    minDistance = distance;
                    nearestNeighborPosition = trace.Position;
                }
            }

            return nearestNeighborPosition;
        }

        private double CalculateEuclideanDistance(List<AccessPoint> inputSignalStrengths, List<AccessPoint> signalStrengths)
        {
            double distanceSquared = 0;

            foreach (var inputAccessPoint in inputSignalStrengths)
            {
                var matchingAccessPoint = signalStrengths.FirstOrDefault(accessPoint => accessPoint.Id == inputAccessPoint.Id);

                if (matchingAccessPoint != null)
                {

                    // Median nutzen
                    int signalStrengthDiff = inputAccessPoint.SignalStrength - matchingAccessPoint.MedianSignalStrength;

                    // Oder jeden einzelnen Wert nutzen
                    // int signalStrengthDiff = inputAccessPoint.SignalStrength - matchingAccessPoint.SignalStrength;

                    distanceSquared += signalStrengthDiff * signalStrengthDiff;
                }
                // Wenn der Access Point nicht gefunden wird, wird er ignoriert.
            }
            var result = Math.Sqrt(distanceSquared);
            return result;
        }
    }
}
