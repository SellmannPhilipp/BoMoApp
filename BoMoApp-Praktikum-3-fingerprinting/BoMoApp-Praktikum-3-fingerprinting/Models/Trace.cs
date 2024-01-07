using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BoMoApp_Praktikum_3_fingerprinting.Models
{
    public class Trace
    {
        public Position Position { get; set; }
        public List<AccessPoint> AccessPoints { get; set; }
    }
}
