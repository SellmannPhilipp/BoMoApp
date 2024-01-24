import matplotlib.pyplot as plt


def readFile(path):
    liste = []
    with open(path, 'r') as datei:
        
        for zeile in datei:
            help = zeile.split("\n")
            help = help[0].replace(",", ".")
            liste.append(float(help))
    return liste


def doGraph(path):
    testliste = readFile(path)
    yList = []

    testliste.sort()

    for i in range(len(testliste)):
        yList.append((i / len(testliste)))

    ax = plt.subplot(111)
    ax.set_yticks([0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1])
    # ax.step(testliste, yList, label = path)
    ax.plot(testliste, yList, label = path)
    print(testliste)
    box = ax.get_position()
    ax.set_position([box.x0, box.y0 + box.height * 0.1, box.width, box.height * 0.9])

    ax.legend(loc='upper center', bbox_to_anchor=(0.5, -0.3), fancybox=True, shadow=True, ncol=6)

    plt.ylabel('Wahrscheinlichkeit')
    plt.xlabel('Abweichung in [m]')

# ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
# ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
# ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

# doGraph("Ergebnisse k3 empirisch.txt")
# doGraph("Ergebnisse k3 model.txt")
# doGraph("Ergebnisse nn empirisch.txt")
# doGraph("Ergebnisse nn model.txt")
    
# doGraph("emp_k1.txt")
# doGraph("emp_k2.txt")
# doGraph("emp_k3.txt")
# doGraph("emp_k4.txt")
# doGraph("emp_k5.txt")
# doGraph("emp_k6.txt")
# doGraph("emp_k7.txt")
# doGraph("emp_k8.txt")

doGraph("mod_k1.txt")
doGraph("mod_k2.txt")
doGraph("mod_k3.txt")
doGraph("mod_k4.txt")
doGraph("mod_k5.txt")
doGraph("mod_k6.txt")
doGraph("mod_k7.txt")
doGraph("mod_k8.txt")

plt.show()