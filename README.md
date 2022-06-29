# LocationAwareSystem
Location aware project providing the closest point of interest through privacy mechanisms

### About the project:

The project implements a **Location-Based Service (LBS)** platform that provides spatial information on 
the presence of car parks close to the user, with privacy management mechanisms. 
In particular, the LBS receives a GPS coordinate (exact, or a reference area), and returns the **Point of
of Interest (PoI** closest to the position provided as input.

The focus of the project is on the implementation of **privacy mechanisms** of the spatial data, to reduce
the risk related to the tracking of the user's trajectory by the LBS.

Specifically, the three main components:

  - **User device (possibly a smartphone)**: retrieves the GPS position and sends it via an encrypted channel 
    to a trusted server, together with the user id. It receives information from the LBS about the
    nearest car park, displaying it on the map.

  - **Trusted server**: implements location-privacy mechanisms (see below)
    and anonymises the user request by replacing the id with a pseudonym.
    We assume the availability of a finite number of pseudonyms, equal to K < 5.
    The new request is forwarded to the LBS.

  - **Location Based Service (LBS)**: receives a request <id,GPS> and
    retrieves the position of the nearest car park to the one provided as input.
    The LBS stores the requests received on the platform on an internal DB,
    and on the basis of these it tries to identify the real position of the user (or
    better, the pseudonym-location association), for example by means of spatial clustering. 
    It is assumed that the LBS is not familiar with the technique of location privacy adopted by the trusted surver. 
    The LBS also has a Web dashboard through which it displays the GPS positions received by the user.

------------------------------------------------------------------------------------------------------

### Built with:

    Node.js
    Express.js
    Javascript
    PostgreSQL
    PostGIS
    QGIS
    OpenLayers - CSS
    Handblebars - HTML
    Python
    Android Studio
    Google Maps
    FakeGPS

------------------------------------------------------------------------------------------------------

###  Project Folder:

 - **BS** - Elaborated version Android app
 - **BSbasic** - Basic version Android app
 - **Location Based Services (LBS)** - Based on Node.js
 - **server_trusted.py** - Based on Python

------------------------------------------------------------------------------------------------------

###  Getting started:

Prerequisites: Python, Node, Postgres, Postgis and Android Studio. 

Create the two Postgres databases

------------------------------------------------------------------------------------------------------

###  Installation

1) Download the project

2) Change IP address using the local ones - Do not modify ports number

	- Android Studio: Enter current Computer IP Addres in file *'MessageSender'*
	- Server Trusted: Enter Smartphone and Computer Ip Address in file *'server_trusted.py'*
	- LBS: Enter Smartphone IP Address in file *'server.js'*

3) Execute Server Trusted with command

        py sercli.py

4) Change directory in LBS folder and execute the following commands:

        npm install express
        npm install pg
        node server.js

5) From Android Studio execute application on the Smartphone (verify correctness of API version)

6) From a brower surf to the following link *"localhost:3000"*
