package com.fyp.electricvehicle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity{
    ListView listview;
    ArrayList<charginpoitnsdataclass> thelist;
    ArrayAdapter<charginpoitnsdataclass> adapter;
    EditText input;
    String BuildingNameNotNull,BuildingNumberNotNull,ThoroughfareNotNull;
    public double lat,longi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview=findViewById(R.id.listview);
        thelist=new ArrayList<>();
        listview.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent=new Intent(MainActivity.this,MapsActivity.class);
            MapsActivity.thedata=thelist;
            intent.putExtra("position",position);
            startActivity(intent);
        });
    }
    public void getdata(View view) throws UnsupportedEncodingException {
        input = findViewById(R.id.input);

        String PostCode = URLEncoder.encode(input.getText().toString(), StandardCharsets.UTF_8.toString());
        String url="https://chargepoints.dft.gov.uk/api/retrieve/registry/postcode/"+PostCode+"/dist/10/limit/10/format/json";
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject jsonObject;
                jsonObject = new JSONObject(response);
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
                    if(BuildingName != "null")
                    {
                        BuildingNameNotNull = BuildingName + ", " ;
                    }
                    else{
                        BuildingNameNotNull ="";
                    }
                    if(BuildingNumber != "null")
                    {
                        BuildingNumberNotNull = BuildingNumber + ", " ;
                    }
                    else{
                        BuildingNumberNotNull ="";
                    }
                    if(Thoroughfare != "null")
                    {
                        ThoroughfareNotNull = Thoroughfare + ", " ;
                    }
                    else{
                        ThoroughfareNotNull ="";
                    }
                    String completeadress = BuildingNameNotNull + BuildingNumberNotNull + ThoroughfareNotNull + Street;
                    Location startPoint=new Location("locationA");
                    startPoint.setLatitude(lat);
                    startPoint.setLongitude(longi);
                    Location endPoint=new Location("locationA");
                    endPoint.setLatitude(Double.valueOf(Latitude));
                    endPoint.setLongitude(Double.valueOf(Longitude));
                    double distance=startPoint.distanceTo(endPoint);

                    Log.d("distance",distance+"");
                        thelist.add(new charginpoitnsdataclass(ChargeDeviceName, ChargeDeviceRef,Latitude,Longitude, completeadress));
                    }
                adapter = new adapterforchargingdevice(MainActivity.this, thelist);
                listview.setAdapter(adapter);

            } catch (JSONException e) {

                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
            Toast.makeText(MainActivity.this, "" + error, Toast.LENGTH_SHORT).show();
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
}