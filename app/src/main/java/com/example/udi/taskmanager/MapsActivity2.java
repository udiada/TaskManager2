package com.example.udi.taskmanager;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button btnGo,btnSave,btnCancel;
    private EditText etToSearch;
    private LatLng loc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        btnCancel=(Button)findViewById(R.id.btnCancel);
        btnSave=(Button)findViewById(R.id.btnSave);
        btnGo=(Button)findViewById(R.id.btnGo);
        btnCancel=(Button)findViewById(R.id.btnCancel);
        etToSearch=(EditText)findViewById(R.id.etToSreach);


        btnGo.setOnClickListener(clickListener);
        btnCancel.setOnClickListener(clickListener);
        btnSave.setOnClickListener(clickListener);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.btnGo:
                    search(etToSearch.getText().toString());
                    break;
                case R.id.btnSave:
                    Intent intent=new Intent();
                    intent.putExtra("lat",loc.latitude);
                    intent.putExtra("lng",loc.longitude);
                    setResult(RESULT_OK,intent);
                    finish();
                    break;
                case R.id.btnCancel:
                   // setResult(RESULT_OK,intent);
                    finish();
                    break;
            }
        }
    };

    private void search(final String s) {
        AsyncTask<Void,Integer,List<Address>>  asyncTask = new AsyncTask<Void, Integer, List<Address>>() {

            List<Address> location;
            Geocoder geocode;

            @Override
            protected void onPreExecute() {
                location=null;
                geocode=new Geocoder(MapsActivity2.this, Locale.getDefault());    //Locale.getDefault()) = lang of
                super.onPreExecute();
            }

            @Override
            protected List<Address> doInBackground(Void... params) {
                try {
                    location=geocode.getFromLocationName(s,2);   // = the max number of results
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return location;
            }

            @Override
            protected void onPostExecute(List<Address> addresses) {
                super.onPostExecute(addresses);

                for (Address a:addresses)
                {
                    loc=new LatLng(a.getLatitude(),a.getLongitude());
                    MarkerOptions m=new MarkerOptions().position(loc).title(s); // or a.  this is the title of the point //Marker = siman hablon above the point
                    mMap.addMarker(m);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,16));    // 16=size of zoom
                }
            }
        } ;
        asyncTask.execute();
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
        mMap.getUiSettings().setCompassEnabled(true); // add compas to map
        mMap.getUiSettings().setZoomControlsEnabled(true); // add zoom control
        // Add a marker in Sydney and move the camera
        LatLng haifa = new LatLng(32.48, 34.59);
        mMap.addMarker(new MarkerOptions().position(haifa).title("Marker in Haifa"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(haifa));
    }
}
