package com.example.nearward;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Lastscreen extends AppCompatActivity {

    TextView locationText;
    ArrayList<HashMap<String, String>> contactList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lastscreen);

        locationText=findViewById(R.id.textView2);
        contactList = new ArrayList<>();

        Intent intent = getIntent();
        String data = intent.getStringExtra("jsonObject");
        locationText.setText(data);

        try {

            JSONObject jsonObj = new JSONObject(data);

            // Getting JSON Array node
            JSONArray contacts = jsonObj.getJSONArray("data");

          //  locationText.setText(data);

            // looping through All Contacts
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject c = contacts.getJSONObject(i);
                String id = c.getString("_id");
                String name = c.getString("Name");
                String address = c.getString("Address");

                String lat = c.getString("lat");
                String lon = c.getString("long");
                String beds = c.getString("bedcount");



                // Phone node is JSON Object
                // tmp hash map for single contact
                HashMap<String, String> contact = new HashMap<>();

                contact.put("id", id);
                contact.put("name", name);
                contact.put("address", address);
                contact.put("lat", lat);
                contact.put("lon", lon);
                contact.put("bedcount", beds);


                // adding contact to contact list
                contactList.add(contact);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
