package com.fyp.electricvehicle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Logger;
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    ListView listview;
    ArrayList<charginpoitnsdataclass> thelist;
    ArrayAdapter<charginpoitnsdataclass> adapter;
    boolean flag=false;
    LocationListener locationListener;
    LocationManager locationManager;
    EditText input;
    public double lat,longi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview=findViewById(R.id.listview);
        thelist=new ArrayList<>();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,MapsActivity.class);
                MapsActivity.thedata=thelist;
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
    }
    public void getdata(View view) throws UnsupportedEncodingException {
        input = findViewById(R.id.input);
        String PostCode = URLEncoder.encode(input.getText().toString(), StandardCharsets.UTF_8.toString());
        //String url="https://chargepoints.dft.gov.uk/api/retrieve/registry/lat/"+latitude+"/long/"+longitude+"/dist/1/format/json";
        String url="https://chargepoints.dft.gov.uk/api/retrieve/registry/postcode/"+PostCode+"/dist/10/limit/10/format/json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject;
                    jsonObject = new JSONObject(response);
                    // CustomProgressDialog.hide();
                    JSONArray array = jsonObject.getJSONArray("ChargeDevice");
                    Log.d("objext", response);
                    thelist = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        String ChargeDeviceRef = array.getJSONObject(i).optString("ChargeDeviceRef");
                        String ChargeDeviceName = array.getJSONObject(i).optString("ChargeDeviceName");
                        JSONObject ChargeDeviceLocation = array.getJSONObject(i).getJSONObject("ChargeDeviceLocation");
                        Log.d("ChargeDeviceLocation",ChargeDeviceLocation+"");
                        String Latitude = ChargeDeviceLocation.optString("Latitude");
                        String Longitude = ChargeDeviceLocation.optString("Longitude");
                        Log.d("lat",Latitude);
                        Log.d("longi",Longitude);
                        JSONObject Address = ChargeDeviceLocation.getJSONObject("Address");
                        String BuildingName = Address.optString("BuildingName");
                        String BuildingNumber = Address.optString("BuildingNumber");
                        String Thoroughfare = Address.optString("Thoroughfare");
                        String Street = Address.optString("Street");
                        String completeadress = BuildingName + "," + BuildingNumber + "," + Thoroughfare + "," + Street;
                        Location startPoint=new Location("locationA");
                        startPoint.setLatitude(lat);
                        startPoint.setLongitude(longi);
                        Location endPoint=new Location("locationA");
                        endPoint.setLatitude(Double.valueOf(Latitude));
                        endPoint.setLongitude(Double.valueOf(Longitude));
                        double distance=startPoint.distanceTo(endPoint);
                        double kmdistance=distance/1000;
                        Log.d("distance",distance+"");
                     //   if(kmdistance<160) {
                            thelist.add(new charginpoitnsdataclass(ChargeDeviceName, ChargeDeviceRef,Latitude,Longitude, completeadress));
                       // }
                       // else
                        //{

                        //}
                        }
                    adapter = new adapterforchargingdevice(MainActivity.this, thelist);
                    listview.setAdapter(adapter);
                    //  CustomProgressDialog.hide();
//                    recycler_view_one.setNestedScrollingEnabled(false);
//                    new SessionManager(Home.this).setKeyAllProducts(allproductsdata);
                } catch (JSONException e) {
                    //   CustomProgressDialog.hide();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // CustomProgressDialog.hide();
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(request);
        //10000 is the time in milliseconds and is equal to 10 sec
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rQueue.add(request);

    }
    public void centreMapOnLocation(Location location, String title){
        flag=true;
        lat=location.getLatitude();
        longi=location.getLongitude();
        Log.d("longi",longi+"");
        LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                centreMapOnLocation(lastKnownLocation,"Your Location");
            }
        }
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Intent intent = getIntent();
        if (intent.getIntExtra("Place Number", 0) == 0) {
            // Zoom into users location
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (flag) {

                    } else {
                        centreMapOnLocation(location, "Your Location");
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {
                }

                @Override
                public void onProviderEnabled(String s) {
                }

                @Override
                public void onProviderDisabled(String s) {
                }
            };
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                centreMapOnLocation(lastKnownLocation, "Your Location");
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }
}