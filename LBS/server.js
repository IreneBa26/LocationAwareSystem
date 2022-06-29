'use strict';

const {Client} = require('pg')

// ClientParcheggi connects to Parking Database
const clientParcheggi = new Client({
    user: "postgres",
    password: "admin",
    host: "localhost",
    port: 5432,
    database: "parcheggi"
})

//clientInternalDB connects to internalDB
const clientInternalDB = new Client({
    user: "postgres",
    password: "admin",
    host: "localhost",
    port: 5432,
    database: "internalDB"
})

var dbpark="";
var dbInt="";

async function exec(){
    try {

    //clients connection
    await clientParcheggi.connect();
    await clientInternalDB.connect();

    // Retrieving data from through query
    var result = await clientParcheggi.query("SELECT name, ST_X(geom) as x, ST_Y(geom) as y FROM parcheggidata");
    for (var i = 0; i < result.rows.length; i++) {
        //console.log(result.rows[i].name + ' ' + result.rows[i].x+' '+result.rows[i].y+ '; ');
        dbpark = dbpark +result.rows[i].name + ' ' + result.rows[i].x+' '+result.rows[i].y+ '; ';
    } 
    var resultt = await clientInternalDB.query("SELECT id, ST_x(geom) as x, ST_y(geom) as y from \"lbsDB\"");
    for (var j = 0; j < resultt.rows.length; j++) {
        //console.log(result.rows[j].id + ' ' + result.rows[j].x+' '+result.rows[j].y+ '; ');
        dbInt = dbInt +resultt.rows[j].id + ' ' + resultt.rows[j].x+' '+resultt.rows[j].y+ ' ; ';
    }

    }
    catch(ex) {
        console.log("smth wrong happened "+ex)
    }
}
exec();


//Instantiating Express variables

var Promise = global.Promise || require('promise');

var express = require('express'),
    exphbs  = require('./'), // "express-handlebars"
    helpers = require('./lib/helpers');

var app = express();

//Creating `ExpressHandlebars` instance with default layout
var hbs = exphbs.create({
    helpers      : helpers,

    partialsDir: [
        'shared/templates/',
        'views/partials/'
    ]
});

// Registering the 'hbs' as display engine using 'engine()' function
app.engine('handlebars', hbs.engine);
app.set('view engine', 'handlebars');

// Middleware for showing shared client models to pages
function exposeTemplates(req, res, next) {

    // Using 'ExpressHandlebars' instance to obtain precompiled models
    hbs.getTemplates('shared/templates/', {
        cache      : app.enabled('view cache'),
        precompiled: true
    }).then(function (templates) {

        // Removing '.handlebars' extensions from templates names
        var extRegex = new RegExp(hbs.extname + '$');

        // Creating matrix of models accessible through 'res.locals.templates'
        templates = Object.keys(templates).map(function (name) {
            return {
                name    : name.replace(extRegex, ''),
                template: templates[name]
            };
        });

        // Exposing models when rendering the view.
        if (templates.length) {
            res.locals.templates = templates;
        }

        setImmediate(next);
    })
    .catch(next);
}


var net = require('net');

var inputString;

// Server connection
var server = net.createServer(function(socket) {
    socket.write('Echo server\r\n');
    socket.on('data', function(data){
    });
});

var client = new net.Socket();

server.listen(7900, '192.168.1.13');

// Error prevention
server.on("error", err =>
{
   console.log("error handled, error is : %s",err);
});

server.on("connection",function(socket)
{
    var remoteAddress = socket.remoteAddress;
    //Signalling connection is active
    console.log("new client connection %s",remoteAddress);

    socket.on('data', function(data){
        //console.log(data);
        inputString = data.toString();
        var arr = inputString.split(" ");

        //socket.write('messages', function(err) { socket.end(); });
        client.connect(7801, '192.168.1.82', function() {

            //Obtaining Point Of Interest (POI)
            var x = execute(arr);
            console.log('Sending POI');
            //client.write('latitude: '+ lat +' and longitude: ' + lon);
            //client.end();
        });
        client.on("error", err =>
        {
            client.destroy();
            console.log("error handled, error is : %s",err);
        });
        
        //socket.end();
        socket.destroy();

    });
    socket.on("error", err =>
    {
        console.log("Incorrect data reception from Python server. See more: %s",err);
        socket.destroy();
    });


    
});

async function execute(arr){
    try {
    //console.log("execute Connected successfully")
    // Analysing received coordinates
    // Inserting coordinates into internalDB through InsertLatLong method

    var lat = arr[1];
    var lon = arr[2];
    console.log(arr[0] + ' latitude: ' + lat + ' and longitude: '+ lon);
    var cord = lon + ' ' + lat;
    insertLatLong(arr[0], arr[1], arr[2]);
    
    var lat2 = arr[5];
    var lon2 = arr[6];
    console.log(arr[4]+' latitude: ' + lat2 + ' and longitude: ' + lon2);
    var cord2 = lon2 + ' ' + lat2;
    insertLatLong(arr[4], arr[5], arr[6]);

    var lat3 = arr[9];
    var lon3 = arr[10];
    console.log(arr[8]+' latitude: '+lat3+' and longitude: '+lon3);
    var cord3 = lon 3 + ' ' + lat3;
    insertLatLong(arr[8], arr[9], arr[10]);

    var lat4 = arr[13];
    var lon4 = arr[14];
    console.log(arr[12]+' latitude: ' + lat4 + ' and longitude: ' + lon4);
    var cord4= lon4 + ' ' + lat4;
    insertLatLong(arr[12], arr[13], arr[14]);

    var lat5 = arr[17];
    var lon5 = arr[18];
    console.log(arr[16] + ' latitude: ' + lat5 + ' and longitude: ' + lon5);
    var cord5 = lon5+' '+lat5;
    insertLatLong(arr[16], arr[17], arr[18]);
    
    //const {rows} = await clientParcheggi.query("WITH MAXIMAL_DISTANCE(name, min) AS (select name, MIN(ST_Distance(ST_GeomFromText('POINT("+cord+")',4326), geom)) from parcheggidata GROUP BY name) SELECT name, min FROM MAXIMAL_DISTANCE WHERE (min = (SELECT MIN(min) FROM MAXIMAL_DISTANCE)); ")
    //const {rows} = await clientParcheggi.query("WITH POINTS(name, min) AS (select name, MIN(ST_Distance(ST_GeomFromText('POINT("+cord+")',4326), geom)) from parcheggidata GROUP BY name) , POINT(name, min) AS (SELECT name, min FROM POINTS WHERE (min = (SELECT MIN(min) FROM POINTS))) , COORDINATES (name, x, y) AS (SELECT name, ST_X(geom), ST_Y(geom) FROM parcheggidata WHERE parcheggidata.name = ( SELECT name FROM POINT ))    SELECT coord.name, coord.x, coord.y, min FROM COORDINATES coord, POINT poi  WHERE coord.name = poi.name;");

    // Query with five points received as input

    const result = await clientParcheggi.query("WITH POINTS(name, min) AS (select name, MIN(ST_Distance(ST_GeomFromText('POINT("+cord+")',4326), geom)) from parcheggidata GROUP BY name) , POINT(name, min) AS (SELECT name, min FROM POINTS WHERE (min = (SELECT MIN(min) FROM POINTS))), COORD(name, min) AS (SELECT POINT.name, POINT.min FROM POINTS, POINT WHERE POINTS.name = POINT.name) SELECT parcheggidata.name, ST_X(geom) as x, ST_Y(geom) as y FROM parcheggidata, COORD WHERE parcheggidata.name = COORD.name");
    const result2 = await clientParcheggi.query("WITH POINTS(name, min) AS (select name, MIN(ST_Distance(ST_GeomFromText('POINT("+cord2+")',4326), geom)) from parcheggidata GROUP BY name) , POINT(name, min) AS (SELECT name, min FROM POINTS WHERE (min = (SELECT MIN(min) FROM POINTS))), COORD(name, min) AS (SELECT POINT.name, POINT.min FROM POINTS, POINT WHERE POINTS.name = POINT.name) SELECT parcheggidata.name, ST_X(geom) as x, ST_Y(geom) as y FROM parcheggidata, COORD WHERE parcheggidata.name = COORD.name");
    const result3 = await clientParcheggi.query("WITH POINTS(name, min) AS (select name, MIN(ST_Distance(ST_GeomFromText('POINT("+cord3+")',4326), geom)) from parcheggidata GROUP BY name) , POINT(name, min) AS (SELECT name, min FROM POINTS WHERE (min = (SELECT MIN(min) FROM POINTS))), COORD(name, min) AS (SELECT POINT.name, POINT.min FROM POINTS, POINT WHERE POINTS.name = POINT.name) SELECT parcheggidata.name, ST_X(geom) as x, ST_Y(geom) as y FROM parcheggidata, COORD WHERE parcheggidata.name = COORD.name");
    const result4 = await clientParcheggi.query("WITH POINTS(name, min) AS (select name, MIN(ST_Distance(ST_GeomFromText('POINT("+cord4+")',4326), geom)) from parcheggidata GROUP BY name) , POINT(name, min) AS (SELECT name, min FROM POINTS WHERE (min = (SELECT MIN(min) FROM POINTS))), COORD(name, min) AS (SELECT POINT.name, POINT.min FROM POINTS, POINT WHERE POINTS.name = POINT.name) SELECT parcheggidata.name, ST_X(geom) as x, ST_Y(geom) as y FROM parcheggidata, COORD WHERE parcheggidata.name = COORD.name");
    const result5 = await clientParcheggi.query("WITH POINTS(name, min) AS (select name, MIN(ST_Distance(ST_GeomFromText('POINT("+cord5+")',4326), geom)) from parcheggidata GROUP BY name) , POINT(name, min) AS (SELECT name, min FROM POINTS WHERE (min = (SELECT MIN(min) FROM POINTS))), COORD(name, min) AS (SELECT POINT.name, POINT.min FROM POINTS, POINT WHERE POINTS.name = POINT.name) SELECT parcheggidata.name, ST_X(geom) as x, ST_Y(geom) as y FROM parcheggidata, COORD WHERE parcheggidata.name = COORD.name");
    var points='GPS ';

    //ora scandisco tutti i risultati ottenuti dalle query e li inserisco in un'unica stringa

    for (var i = 0; i < result.rows.length; i++) {
        //console.log(result.rows[i].name + ' ' + result.rows[i].x+' '+result.rows[i].y+ '; ');
        points = points + result.rows[i].name + ' ' + result.rows[i].x + ' ' + result.rows[i].y + '; ';
    }
    points += 'GPS ';
    for (var k = 0; k < result2.rows.length; k++) {
        //console.log(result2.rows[k].name + ' ' + result2.rows[k].x+' '+result2.rows[k].y+ '; ');
        points = points + result2.rows[k].name + ' ' + result2.rows[k].x + ' ' + result2.rows[k].y + '; ';
    }
    points += 'GPS ';
    for (var t = 0; t < result3.rows.length; t++) {
        //console.log(result3.rows[t].name + ' ' + result3.rows[t].x+' '+result3.rows[t].y+ '; ');
        points = points + result3.rows[t].name + ' ' + result3.rows[t].x + ' ' + result3.rows[t].y + '; ';
    }
    points += 'GPS ';
    for (var l = 0; l < result4.rows.length; l++) {
        //console.log(result4.rows[l].name + ' ' + result4.rows[l].x+' '+result4.rows[l].y+ '; ');
        points = points + result4.rows[l].name + ' ' + result4.rows[l].x + ' ' + result4.rows[l].y + '; ';
    }
    points += 'GPS ';
    for (var p = 0; p < result5.rows.length; p++) {
        //console.log(result5.rows[p].name + ' ' + result5.rows[p].x+' '+result5.rows[p].y+ '; ');
        points = points + result5.rows[p].name + ' ' + result5.rows[p].x + ' ' + result5.rows[p].y + '; ';
    }

    // Sending final string containing all coordinates
    console.log('points: '+ points);
    client.write(points);
    client.destroy();

    return 'x';
    }
    catch(ex) {
        console.log("Something wrong happened " + ex)
    }
    finally {
        //await clientParcheggi.end()
        //console.log("execute Client disconnected successfully")
    }
}

async function insertLatLong(id, lat, long){
    try {
    //console.log("insert LATLONG Connected successfully")
    
    /*
    String ID is the pseudonym sent from server to the LBS
    ST_GeomFromText is a POSTGis method: takes a text representation and a spatial reference ID and returns a geometry object in Oracle and SQLite
    Inserting data into lbsDB table
    */
    var idIns = id;
    var latIns = lat;
    var longIns = long;
    //console.log("Received data: "+idIns+" "+latIns+" "+longIns);
    
    await clientInternalDB.query("INSERT INTO \"lbsDB\"(id, geom) VALUES ('"+idIns+"',ST_GeomFromText('POINT("+latIns+" "+longIns+")',4326))");

    }
    catch(ex) {
        console.log("Something wrong happened " + ex)
    }
    finally {
        //await clientInternalDB.end();
        //console.log("insertLatLong Client disconnected successfully")
    }
}

// Home is the landing page
app.get('/', function (req, res) {
    res.render('home', {
        title: 'Home'
    });
});

app.get('/coordinateGPS', function (req, res) {
    res.render('coordinateGPS', {
        title: 'coordinateGPS',

        // This `message` will be transformed by our `coordinateGPS()` helper.
        message: 'Received coordinates: '+ inputString + ' _ ' + dbpark + ' _ ' + dbInt
    });
});

app.use(express.static('public/'));

app.listen(3000, function () {
    console.log('express-handlebars example server listening on: 3000');
});
