package com.example.utente.bsbasic;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private String TAG = "[DebApp]MainActivity";
    private int cont=0;
    private String scelta= "9";
    private String idSmartphone;
    //private String idSmartphone="3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        idSmartphone = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Thread myThread = new Thread(new MyServerThread());
        myThread.start();

        Thread myThread1 = new Thread(new MyServerThread1());
        myThread1.start();


        setContentView(R.layout.activity_main);


    }

    class MyServerThread implements Runnable {
        Socket s;
        ServerSocket ss;
        InputStreamReader isr;
        BufferedReader bufferedReader;
        Handler h = new Handler();
        String message = "";

        @Override
        public void run() {
            try {
                ss = new ServerSocket(7802);
                while (true) {

                    s = ss.accept();
                    isr = new InputStreamReader(s.getInputStream());
                    bufferedReader = new BufferedReader(isr);
                    message = bufferedReader.readLine();

                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("Ho memorizzato: "+ message);
                            scelta = message;
                            //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }

                    });


                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    class MyServerThread1 implements Runnable {

        Socket s1;
        ServerSocket ss1;
        InputStreamReader isr1;
        BufferedReader bufferedReader1;
        Handler h = new Handler();
        String message1 = "";



        @Override
        public void run() {
            try {
                ss1 = new ServerSocket(7801);
                while (true) {



                    s1 = ss1.accept();
                    isr1 = new InputStreamReader(s1.getInputStream());
                    bufferedReader1 = new BufferedReader(isr1);
                    message1 = bufferedReader1.readLine();

                    h.post(new Runnable() {
                        @Override
                        public void run() {

                            System.out.println("Scelta: " + scelta);
                            String messag[] = message1.split("GPS");
                            String s = "";
                            String parcheggio="";
                            for (int k = 1; k < messag.length; k++) {
                                s += messag[k];
                            }
                            System.out.println("STAMPA s: "+s);
                            if (scelta.equals("0")) {
                                System.out.println("scelta 0");
                                Toast.makeText(getApplicationContext(), messag[1], Toast.LENGTH_SHORT).show();
                                parcheggio=messag[1];
                            } else if (scelta.equals("1")) {
                                System.out.println("scelta 1");
                                Toast.makeText(getApplicationContext(), messag[2], Toast.LENGTH_SHORT).show();
                                parcheggio=messag[2];
                            } else if (scelta.equals("2")) {
                                System.out.println("scelta 2");
                                Toast.makeText(getApplicationContext(), messag[3], Toast.LENGTH_SHORT).show();
                                parcheggio=messag[3];
                            } else if (scelta.equals("3")) {
                                System.out.println("scelta 3");
                                Toast.makeText(getApplicationContext(), messag[4], Toast.LENGTH_SHORT).show();
                                parcheggio=messag[4];
                            } else if (scelta.equals("4")) {
                                System.out.println("scelta 4");
                                Toast.makeText(getApplicationContext(), messag[5], Toast.LENGTH_SHORT).show();
                                parcheggio=messag[5];
                            }
                            scelta = "";
                            message1= "";
                            String park="";


                            String stringheParcheggi [] = parcheggio.split(";");
                            if (stringheParcheggi.length>1) {

                                for (int u=0; u<stringheParcheggi.length; u++) {
                                    String app = stringheParcheggi[u];
                                    String app2= app.replace(" ", "");

                                    if (!app2.equals("")) {
                                        park = app+";";
                                        park="";
                                    }

                                }
                            }
                            Button senn = (Button) findViewById(R.id.SEND);
                            senn.setEnabled(true);


                        }
                    });
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void send(View v) {
        double lat = 0;
        double lon = 0;
        GpsTracker gt = new GpsTracker(getApplicationContext());
        Location l = gt.getLocation();
        if( l == null){
            Toast.makeText(getApplicationContext(),"Accendi la localizzazione! (GPS unable to get Value)",Toast.LENGTH_SHORT).show();
            //Log.d("CREATION","GPS unable to get Value");

        }else {
            cont++;
            lat = l.getLatitude();
            lon = l.getLongitude();
            //Toast.makeText(getApplicationContext(),idSmartphone+", Lat = "+lat+"\n lon = "+lon+" "+cont,Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"Coordinate dello smartphone inviate:\n Lat = "+lat+"\n Lon = "+lon,Toast.LENGTH_SHORT).show();

            //Log.d("CREATION","GPS Lat = "+lat+"\n lon = "+lon);
            MessageSender messageSender = new MessageSender();
            //messageSender.execute("GPS Lat = "+lat+" lon = "+lon+" "+cont);
            messageSender.execute(idSmartphone+", Lat = "+lat+" lon = "+lon+ " ");

            Button sen = (Button) findViewById(R.id.SEND);
            sen.setEnabled(false);
        }



    }


}