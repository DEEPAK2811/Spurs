package com.example.nearward;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    Button getLocationBtn,sndbtn,btbtn;
    TextView locationText,locationText2,textclr;
    EditText idedit,sndtxt;
    ImageView im1;
    LocationManager locationManager;
    private static final String TAG3 = "LA";
    //    RequestQueue requestQueue;

    String idd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        //receive the address of the bluetooth device

        //     requestQueue = Volley.newRequestQueue(this);

        getLocationBtn = (Button)findViewById(R.id.getLocationBtn);
        sndbtn=findViewById(R.id.sndbtn1);
        btbtn=findViewById(R.id.btlist);
        sndtxt=findViewById(R.id.editText);
        locationText = (TextView)findViewById(R.id.locationText);
        locationText2 = (TextView)findViewById(R.id.locationText2);
        textclr=findViewById(R.id.textalert);
        im1=findViewById(R.id.colorimg);
        idedit=findViewById(R.id.getID);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        idd = idedit.getText().toString();
        String iddd = idd;
        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }
    String lat, lon;
    @Override
    public void onLocationChanged(Location location) {
        lat = Double.toString(location.getLatitude());
        lon = Double.toString(location.getLongitude());
        locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());


       //new AsyncTaskRunner().execute();
        //new BackgroundWorker(getApplicationContext()).execute();
        String url = "https://barcoendpoint.herokuapp.com/api/gethospitals?coordinates="+lat+","+lon;


        JsonArrayRequest jsonRequest = new JsonArrayRequest

                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("JsonArray  ::  ",response.toString());

                        // the response is already constructed as a JSONObject!
                        Intent intent = new Intent(MainActivity.this, Lastscreen.class);
                        intent.putExtra("jsonObject", response.toString());
                        startActivity(intent);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);



        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            locationText.setText(locationText.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+
                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
        }catch(Exception e)
        {

        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(MainActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

  /*  private class BackgroundWorker extends AsyncTask<String, Void, String> {

        Context context;
        String result;

        private BackgroundWorker(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            //String register_url = "http://www.axisbank.epizy.com";
            Log.d("ID:", idd);
            String register_url = "https://barcoendpoint.herokuapp.com/api/gethospitals?coordinates="+lat+","+lon;
            try {
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
              OutputStream outputStream = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                String post_data = URLEncoder.encode("X", "UTF-8")+"="+URLEncoder.encode(lat, "UTF-8")
                        +"&"+URLEncoder.encode("Y","UTF-8")+"="+URLEncoder.encode(lon,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();


                Log.d("URL:", register_url);
               outputStream.close();
                try {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    Log.d("encode1", "encoded");
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.ISO_8859_1));
                    Log.d("encode2", "encoded");
                    result = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                     Log.d("initial result",result);
                    //locationText2.setText(result);
                   bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPreExecute() {
            //  alertDialog = new AlertDialog.Builder(context).create();
            //alertDialog.setTitle("Login Status");
            super.onPreExecute();
        }

        @SuppressLint("ResourceAsColor")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("result",result);
            locationText2.setText(result);

            // result
        }


    }

    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }*/



}



/*public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}*/
