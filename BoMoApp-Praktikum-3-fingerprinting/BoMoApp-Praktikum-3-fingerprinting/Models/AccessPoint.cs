using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BoMoApp_Praktikum_3_fingerprinting.Models
{
    public class AccessPoint
    {
        public string Id { get; set; }
        public int SignalStrength { get; set; }
        public double Frequency { get; set; }
        public int Channel { get; set; }
        public int MedianSignalStrength { get; set; }
    }
}
