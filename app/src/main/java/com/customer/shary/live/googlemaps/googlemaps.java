package com.customer.shary.live.googlemaps;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.customer.shary.live.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class googlemaps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private mViewmodelmap mViewmodelmap ;

    Store_map_model model ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlemaps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Log.e("alialialai",getIntent().getExtras().getString("saller_id"));



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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Add a marker in Sydney and move the camera





        mViewmodelmap =new ViewModelProvider(googlemaps.this).get(mViewmodelmap.class);


        mViewmodelmap.getdata(String.valueOf(getIntent().getExtras().getString("saller_id")),getApplicationContext()).observe(googlemaps.this, new Observer<Store_map_model>() {
            @Override
            public void onChanged(Store_map_model store_map_model) {
                if(store_map_model!=null)
                {
                    model =store_map_model;
                }
                // Add a marker in Sydney and move the camera
                LatLng sydney = new LatLng(Double.valueOf(store_map_model.getLatitude()),
                        Double.valueOf(store_map_model.getLongitude()));

                mMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title(store_map_model.getStore_name())
                        .draggable(true));
                        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.a)));
               // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,90));


                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter()
                {
                    @Override
                    public View getInfoWindow(Marker arg0)
                    {
                        return null;
                    }

                    public View getInfoContents(Marker marker)
                    {
                        View v = getLayoutInflater().inflate(R.layout.mapmarker,null);

                        TextView _Store_name = v.findViewById(R.id._Store_name);
                        TextView textView9 =v.findViewById(R.id.textView9);
                        ImageView imageView4 =v.findViewById(R.id.imageView4);
                        _Store_name.setText(model.getStore_name().toString());

                        textView9.setText(model.getStore_description().toString());
                        Glide.with(googlemaps.this).load(model.getStore_image()).into(imageView4);

                        return v;
                    }
                });


            }
        });


    }





}
