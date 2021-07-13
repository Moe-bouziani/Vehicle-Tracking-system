package elhlafi.cs98.whereismycar;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng sydney ;
    private Polyline line;
    private EditText ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
              .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;


        // Add a marker in Sydney and move the camera
       sydney = new LatLng(34.016581803350924, -6.835877961665342);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Morocco"));
       // mMap.setMinZoomPreference(16.0f);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


    }
    public void gethttp(View v)  {
        new Thread(new Runnable() {
            @SuppressLint("WrongViewCast")
            @Override
            public void run() {

        try {
            ID=findViewById(R.id.ID);
            URL urlService = new URL("http://10.0.2.2:3000/mob");
            HttpURLConnection urlCnx = (HttpURLConnection) urlService.openConnection();
            urlCnx.setRequestProperty("Content-Type", "application/json");
            urlCnx.setRequestProperty("Accept", "application/json");
            urlCnx.setRequestMethod("POST");
            urlCnx.setDoOutput(true);
            urlCnx.setDoInput(true);
            urlCnx.connect();
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("id", ID.getText().toString());

            DataOutputStream os = new DataOutputStream( urlCnx.getOutputStream());
            os.writeBytes(jsonParam.toString());
            os.flush();
            os.close();
            InputStream in = new BufferedInputStream(urlCnx.getInputStream());
            Scanner sc = new Scanner(in);
            Gson gson = new GsonBuilder().create();
            final Type CarList = new TypeToken<ArrayList<Car>>() {
            }.getType();
            final List<Car> listcar = gson.fromJson(sc.nextLine(), CarList);
            final StringBuilder s = new StringBuilder();
            final Polyline[] polyline1 = new Polyline[1];
            runOnUiThread(new Runnable() {@Override public void run() {
                PolylineOptions options = new PolylineOptions().width(5).color(Color.RED).geodesic(true);
                for (Car c : listcar) {
                    //System.out.println(c.getLat() +"& " +c.getLongt());
                    LatLng point = new LatLng(c.getLat(),c.getLongt());
                    System.out.println(point);
                    options.add(point);
                }
                line = mMap.addPolyline(options);

                sydney = new LatLng(listcar.get(listcar.size()-1).getLat(),listcar.get(listcar.size()-1).getLongt());
                mMap.addMarker(new MarkerOptions().position(sydney).title("my id is "+listcar.get(listcar.size()-1).getId()+"and my coordinates are :\n  Lat:"+listcar.get(listcar.size()-1).getLat()+"& Longt:"+listcar.get(listcar.size()-1).getLongt() ));

               mMap.setMinZoomPreference(16.0f);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));




            }});} catch(Exception e) {e.printStackTrace();}}}).start();

    }

}
