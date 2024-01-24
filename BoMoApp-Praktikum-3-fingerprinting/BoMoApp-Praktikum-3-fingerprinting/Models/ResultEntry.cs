using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BoMoApp_Praktikum_3_fingerprinting.Models
{
    public class ResultEntry
    {
        public Position OnlinePosition { get; set; }
        public Position OfflinePosition { get; set; }
        public double EuclideanDistance { get; set; }
    }
}
