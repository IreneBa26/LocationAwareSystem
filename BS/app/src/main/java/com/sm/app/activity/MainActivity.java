package com.sm.app.activity;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.provider.Settings.Secure;


import com.sm.app.entity.GpsTracker;
import com.sm.app.entity.MessageSender;
import com.sm.app.fragment.HomeFragment;
import com.sm.app.fragment.InfoFragment;
import com.sm.app.fragment.NewGeofenceFragment;
import com.sm.app.utils.Constant;
import com.sm.app.utils.Controller;
import com.sm.app.R;
import com.sm.app.fragment.ServiceFragment;
import com.sm.app.fragment.CreditsFragment;
import com.sm.app.fragment.FenceListFragment;
import com.sm.app.fragment.MapFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *  Main and unique Activity in the project.
 *  The different screens are contained in Fragments.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
    {

    private String TAG = "[DebApp]MainActivity";
    private int cont=0;
    private String scelta= "9";
    private String idSmartphone;
    //private String idSmartphone="3";
    private NewGeofenceFragment ngf= new NewGeofenceFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        idSmartphone = Secure.getString(getContentResolver(), Secure.ANDROID_ID);

        Thread myThread = new Thread(new MyServerThread());
        myThread.start();

        Thread myThread1 = new Thread(new MyServerThread1());
        myThread1.start();

        // CONTROLLER Istance Initialization
        Controller.getInstance();
        // Set DB Manager
        Controller.setDbManager(getApplicationContext());
        // Resume all fences in Controller ArrayList
        Controller.resumeFencesFromDb();
        // Log of SQLiteDB
        Controller.getLogFenceEntities();


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        HomeFragment fragment = new HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();

        boolean permissionFineLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean permissionSendSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
        ArrayList<String> deniedPermissions = new ArrayList<String>();

        if(!permissionFineLocation)
            deniedPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        if(!permissionSendSMS)
            deniedPermissions.add(Manifest.permission.SEND_SMS);

        if (deniedPermissions.size() > 0) {
            String[] deniedPermissionsArray = new String[deniedPermissions.size()];
            deniedPermissions.toArray(deniedPermissionsArray);
            Controller.requestPermission((AppCompatActivity) this, 1, deniedPermissionsArray);
        }
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
                    System.out.println("SONO NEL SOCKET 1");
                    while (true) {

                        s = ss.accept();
                        isr = new InputStreamReader(s.getInputStream());
                        bufferedReader = new BufferedReader(isr);
                        message = bufferedReader.readLine();
                        System.out.println("messageeeeeeeeeeeeeee : " + message);

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
                    System.out.println("SONO NEL SOCKET 1");
                    while (true) {



                        s1 = ss1.accept();
                        isr1 = new InputStreamReader(s1.getInputStream());
                        bufferedReader1 = new BufferedReader(isr1);
                        message1 = bufferedReader1.readLine();
                        System.out.println("messageeeeeeeeeeeeeee 11111: " + message1);

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

                                System.out.println("parcheggiooooooooooo "+parcheggio);
                                String stringheParcheggi [] = parcheggio.split(";");
                                if (stringheParcheggi.length>1) {

                                    for (int u=0; u<stringheParcheggi.length; u++) {
                                        System.out.println("Stringaaaaaaaa "+stringheParcheggi[u]);
                                        String app = stringheParcheggi[u];
                                        String app2= app.replace(" ", "");

                                        if (!app2.equals("")) {
                                            park = app+";";
                                            ngf.saveChangesInSQLiteDBLatlong(park);
                                            Controller.getLogFenceOnSQLiteDB();
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /* Click on action bar menù. */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_info) {
            Fragment fragment = new InfoFragment();
            String title = Constant.TITLE_VIEW_INFO;
            this.runFragment(fragment, title);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        Fragment fragment = null;
        // Fragment title
        String title = Constant.TITLE_VIEW_APP;

        if (id == R.id.nav_map_home) {
            // Home Fragment
            fragment = new HomeFragment();
            title = Constant.TITLE_VIEW_HOME;
        }else if (id == R.id.nav_map_geofences) {
            // Map Fragment
            fragment = new MapFragment();
            title = Constant.TITLE_VIEW_MAP_GEOFENCES;
        } else if (id == R.id.nav_list_geofences) {
            // Fence List(and add) List
            fragment = new FenceListFragment();
            title = Constant.TITLE_VIEW_LIST_GEOFENCES;
        } else if (id == R.id.nav_list_start_service) {
            // Service Fragment
            fragment = new ServiceFragment();
            title = Constant.TITLE_VIEW_START_SERVICE;
        } else if (id == R.id.nav_info_credits) {
            // Credits Fragment
            fragment = new CreditsFragment();
            title = Constant.TITLE_VIEW_CREDITS;
        }

        this.runFragment(fragment, title);
        return true;
    }

    /* Commit of Fragment */
    private void runFragment(Fragment fragment, String title) {
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onStart() {
        super.onStart();
    };

    @Override
    protected void onRestart() {
        super.onRestart();
    };

    @Override
    protected void onResume() {
        super.onResume();
    };

    @Override
    protected void onPause() {
        super.onPause();
    };

    @Override
    protected void onStop() {
        super.onStop();
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        boolean showWarningToast = false;
        String toShow = "YOU MUST ACCEPT ALL PERMISSIONS!!";
        for(int i=0; i<permissions.length; i++) {
            Log.d(TAG, "Permission: " + permissions[i] + "  |  result:  " + grantResults[i]);
            if (grantResults[i] == PackageManager.PERMISSION_DENIED)
                showWarningToast = true;
        }
        if(showWarningToast)
            Toast.makeText(getApplicationContext(), toShow, Toast.LENGTH_LONG).show();
    }
}
