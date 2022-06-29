import socket
import sys
import time
import random as ran

from random import *

HOST = '192.168.1.13'
PORT = 7800
# PC address and dedicated PC port for communication with Android app.

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# socket.socket: create socket.
# socket.AF_INET: format for IP address request
# socket.SOCK_STREAM: connection initialization.

print('socket created')

# Binding the port to the address
try:
    s.bind((HOST, PORT))
except socket.error as err:
    print('Bind Failed, Error Code: ' + str(err[0]) + ', Message: ' + err[1])
    sys.exit()

print('Socket Bind Success!')

s.listen(10)
# listen(): Initialize TCP listener.
print('Socket is now listening')
pseudonyms = ["Alex", "Barbara", "Carlo", "David", "Ester"]

while 1:
    conn, addr = s.accept()
    # connection to the smartphone
    print('Connect with ' + addr[0] + ':' + str(addr[1]))
    buf = conn.recv(64)
    # buf contains Android App Id and GPS Coordinates

    # Creating socket TCP/IP
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    # Connect the socket to the port where LBS is listening
    server_address = ("192.168.1.13", 7900)
    sock.connect(server_address)

    try:
        # Send the data
        # Point format: " Lat = 44.34268100537564\n lon = 10.935943482778422 "

        # Creating socket TCP/IP
        sockCell = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

        # Id sent to client
        # Binding socket to server port
        server_address_cell = ("192.168.1.82", 7802)
        # Server address of the mobile phone and related port where to send the id
        sockCell.connect(server_address_cell)

        indexArray = 8

        try:
            indexArray = randint(0, 4)
            index = str(indexArray)
            print('Sending the id: ' + index)
            # Returning the id assigned to the mobile phone

            indexApp = index.encode(encoding='UTF-8')
            sockCell.sendall(indexApp)
        finally:
            sockCell.close()

        # Analysing the received coordinates
        newStringThing = buf.decode(encoding='UTF-8')
        print('The coordinates received are: ' + newStringThing)

        indexArray = randint(0, 4)

        # Creating array to manipulate the received string
        arr = newStringThing.split(" ")

        # Latitude and longitude coordinates
        latPoint = float(arr[3])
        lonPoint = float(arr[6])

        # Initializing user in circle 0 (Non existent)
        circle = 0

        if latPoint > 44.48 and latPoint < 44.52:
            if lonPoint > 11.29 and lonPoint < 11.41:
                print("I am in circle 1")
                circle = 1

                # Creating 4 fake points
                lonMin = 11.29
                lonMax = 11.41
                latMin = 44.48
                latMax = 44.52

                # Spatial Cloaking: Decreasing precision by breaking and trimming the coordinates

                latPoi = arr[3].split(".")
                latNumbersAfterComma = latPoi[1]
                latString = latPoi[0] + "." + latNumbersAfterComma[0] + latNumbersAfterComma[1] + latNumbersAfterComma[
                    2]

                lonPoi = arr[6].split(".")
                lonNumbersAfterComma = lonPoi[1]
                lonString = lonPoi[0] + "." + lonNumbersAfterComma[0] + lonNumbersAfterComma[1] + lonNumbersAfterComma[2]

                # Unifier:
                # Creating second part of coordinates by appending random number to unify them with the structure of the random-generated one

                latRandom = ran.uniform(10000000000, 999999999999)
                lonRandom = ran.uniform(10000000000, 999999999999)
                latStringToAppend = str(latRandom)
                lonStringToAppend = str(lonRandom)

                finalLat = latString + latStringToAppend.replace(".", "")
                finalLon = lonString + lonStringToAppend.replace(".", "")

                # finalLat and finalLong contain the new coordinate (to be sent with the artificial one)

        if latPoint > 44.40 and latPoint < 44.60 and circle != 1:
            if lonPoint > 11.05 and lonPoint < 11.65:
                print("I am in circle 2")
                circle = 2
                lonMin = 11.05
                lonMax = 11.65
                latMin = 44.40
                latMax = 44.60

                # Spatial Cloaking

                latPoi = arr[3].split(".")
                latNumbersAfterComma = latPoi[1]
                latString = latPoi[0] + "." + latNumbersAfterComma[0] + latNumbersAfterComma[1]

                lonPoi = arr[6].split(".")
                lonNumbersAfterComma = lonPoi[1]
                lonString = lonPoi[0] + "." + lonNumbersAfterComma[0] + lonNumbersAfterComma[1]

                latRandom = ran.uniform(100000000000, 9999999999999)
                lonRandom = ran.uniform(100000000000, 9999999999999)
                latStringToAppend = str(latRandom)
                lonStringToAppend = str(lonRandom)

                finalLat = latString + latStringToAppend.replace(".", "")
                finalLon = lonString + lonStringToAppend.replace(".", "")

        if latPoint > 43.86 and latPoint < 45.14 and circle != 1 and circle != 2:
            if lonPoint > 9.40 and lonPoint < 13.27:
                print("I am in circle 3")
                circle = 3
                lonMin = 9.40
                lonMax = 13.27
                latMin = 43.86
                latMax = 45.14

                # Spatial Cloaking

                latPoi = arr[3].split(".")
                latNumbersAfterComma = latPoi[1]
                latString = latPoi[0] + "." + latNumbersAfterComma[0]

                lonPoi = arr[6].split(".")
                lonNumbersAfterComma = lonPoi[1]
                lonString = lonPoi[0] + "." + lonNumbersAfterComma[0]

                latRandom = ran.uniform(1000000000000, 99999999999999)
                lonRandom = ran.uniform(1000000000000, 99999999999999)
                latStringToAppend = str(latRandom)
                lonStringToAppend = str(lonRandom)

                finalLat = latString + latStringToAppend.replace(".", "")
                finalLon = lonString + lonStringToAppend.replace(".", "")

        if latPoint > 28.5 and latPoint < 60.5 and circle != 1 and circle != 2 and circle != 3:
            if lonPoint > 4.28 and lonPoint < 18.39:
                print("I am in circle 4")
                circle = 4
                lonMin = 4.28
                lonMax = 18.39
                latMin = 28.5
                latMax = 60.5

                # Spatial Cloaking

                latPoi = arr[3].split(".")
                latNumbersAfterComma = latPoi[1]
                latString = latPoi[0] + "." + latNumbersAfterComma[0]

                lonPoi = arr[6].split(".")
                lonNumbersAfterComma = lonPoi[1]
                lonString = lonPoi[0] + "." + lonNumbersAfterComma[0]

                latRandom = ran.uniform(1000000000000, 99999999999999)
                lonRandom = ran.uniform(1000000000000, 99999999999999)
                latStringToAppend = str(latRandom)
                lonStringToAppend = str(lonRandom)

                finalLat = latString + latStringToAppend.replace(".", "")
                finalLon = lonString + lonStringToAppend.replace(".", "")

        if circle == 0:
            print("I am in circle 5")
            lonMin = -175
            lonMax = 175
            latMin = -84
            latMax = 84

            # Spatial Cloaking

            latPoi = arr[3].split(".")
            latString = latPoi[0] + "."

            lonPoi = arr[6].split(".")
            lonString = lonPoi[0] + "."

            latRandom = ran.uniform(10000000000000, 999999999999999)
            lonRandom = ran.uniform(10000000000000, 999999999999999)
            latStringToAppend = str(latRandom)
            lonStringToAppend = str(lonRandom)

            finalLat = latString + latStringToAppend.replace(".", "")
            finalLon = lonString + lonStringToAppend.replace(".", "")

        # Creating four fake coordinates (a,b,c,d)
        # Calculating longitude and latitude max and min

        indexArraySup = randint(0, 4)
        lonRandom = ran.uniform(lonMin, lonMax)
        latRandom = ran.uniform(latMin, latMax)
        a = pseudonyms[indexArraySup] + " " + str(latRandom) + " " + str(lonRandom)

        indexArraySup = randint(0, 4)
        lonRandom = ran.uniform(lonMin, lonMax)
        latRandom = ran.uniform(latMin, latMax)
        b = pseudonyms[indexArraySup] + " " + str(latRandom) + " " + lonRandomStringa

        indexArraySup = randint(0, 4)
        lonRandom = ran.uniform(lonMin, lonMax)
        latRandom = ran.uniform(latMin, latMax)
        c = pseudonyms[indexArraySup] + " " + str(latRandom) + " " + str(lonRandom)

        indexArraySup = randint(0, 4)
        lonRandom = ran.uniform(lonMin, lonMax)
        latRandom = ran.uniform(latMin, latMax)
        d = pseudonyms[indexArraySup] + " " + str(latRandom) + " " + str(lonRandom)

        # Disturbed coordinates

        coordDisturbed = pseudonyms[indexArray] + " " + finalLat + " " + finalLon

        # Final string containing all five coordinates

        tot = ""
        indexAr = int(index)

        if indexAr == 0:
            tot = coordDisturbed + " ; " + a + " ; " + b + " ; " + c + " ; " + d
        elif indexAr == 1:
            tot = a + " ; " + coordDisturbed + " ; " + b + " ; " + c + " ; " + d
        elif indexAr == 2:
            tot = a + " ; " + b + " ; " + coordDisturbed + " ; " + c + " ; " + d
        elif indexAr == 3:
            tot = a + " ; " + b + " ; " + c + " ; " + coordDisturbed + " ; " + d
        elif indexAr == 4:
            tot = a + " ; " + b + " ; " + c + " ; " + d + " ; " + coordDisturbed

        # Suspending execution before sending coordinates to LBS in order to ensure correct reception and memorization of the index by the Android app
        time.sleep(3)
        print('tot ' + tot + " index: " + index)

        textToLBS = tot.encode(encoding='UTF-8')
        sock.sendall(textToLBS)

        # Suspending execution before closing in order to ensure correct receipt of coordinates by the LBS
        time.sleep(3)
    finally:
        sock.close()

s.close()
