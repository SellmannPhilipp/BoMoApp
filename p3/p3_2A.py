import matplotlib.pyplot as plt

# AP, x_distance, y_signal_strenght
def readFile(path):
    liste = []
    with open(path, 'r') as datei:
        
        for zeile in datei:
            help = zeile.split(",")
            print(len(help))
            for i in range(3, len(help) - 3, 3):
                print(i)
                liste.append((str(help[i]), float(help[i + 1]), float(help[i + 2])))
    return liste

def splitList(liste):
    dic = {}
    for i in liste:
        if i[0] not in dic.keys():
            x = []
            y = []
            x.append(i[1])
            y.append(i[2])
            dic[i[0]] = (x,y)
        else:
            dic[i[0]][0].append(i[1])
            dic[i[0]][1].append(i[2])

    return dic

def doGraph(path):
    testliste = readFile(path)

    dic_test = splitList(testliste)

    ax = plt.subplot(111)

    for ap in dic_test:
        ax.plot(dic_test[ap][0], dic_test[ap][1], label = ap, linestyle='', marker='x')

    box = ax.get_position()
    ax.set_position([box.x0, box.y0 + box.height * 0.1, box.width, box.height * 0.9])

    ax.legend(loc='upper center', bbox_to_anchor=(0.5, 1.2), fancybox=True, shadow=True, ncol=6)

    plt.ylabel('Signal strenght in dBm')
    plt.xlabel('Distance in [m]')

    plt.show()


# ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
# ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
# ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

doGraph("Simulation model-based.txt")