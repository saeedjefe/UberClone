package saeed.example.com.map;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class PassengerActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Button btnDriverRequest;

    // init a boolean value
    private boolean isOrderCanceled = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_maps );
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById( R.id.map );
        mapFragment.getMapAsync( this );

        btnDriverRequest = findViewById( R.id.btnDriverRequest );

        btnDriverRequest.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText( PassengerActivity.this, "hello world" , Toast.LENGTH_LONG ).show();

                if (!isOrderCanceled) {

                    final ProgressDialog progressDialog = new ProgressDialog( PassengerActivity.this );
                    progressDialog.setMessage( "sending" );
                    progressDialog.show();

                    if (ContextCompat.checkSelfPermission( PassengerActivity.this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, locationListener );
                        Location passengerLocation = locationManager.getLastKnownLocation( LocationManager.GPS_PROVIDER );

                        ParseObject parseObject = new ParseObject( "requestCar" );
                        parseObject.put( "username", ParseUser.getCurrentUser().getUsername() );

                        ParseGeoPoint parseGeoPoint = new ParseGeoPoint( passengerLocation.getLatitude(), passengerLocation.getLongitude() );
                        parseObject.put( "location", parseGeoPoint );

                        parseObject.saveInBackground( new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText( PassengerActivity.this, "your request has been sent to your driver", Toast.LENGTH_LONG ).show();
                                    btnDriverRequest.setText( "Cancel The Ride" );

                                    isOrderCanceled = true;



                                } else {
                                    Toast.makeText( PassengerActivity.this, "failed", Toast.LENGTH_LONG ).show();

                                }

                                progressDialog.dismiss();
                            }
                        } );

                    }

                }

                else
                {

                    Toast.makeText(PassengerActivity.this, " towast", Toast.LENGTH_LONG ).show();

                    ParseQuery<ParseObject> parseQuery =  ParseQuery.getQuery( "requestCar" );
                    parseQuery.whereEqualTo( "passenger" ,ParseUser.getCurrentUser().getUsername());
                    parseQuery.findInBackground( new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if(e==null&&objects.size()>0)
                            {
                                for(ParseObject parseObject: objects)
                                {
                                    parseObject.deleteInBackground( new DeleteCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if(e==null)
                                            {
                                                Toast.makeText(PassengerActivity.this, " your request has been successfully canceled", Toast.LENGTH_LONG ).show();

                                                isOrderCanceled = false;

                                                btnDriverRequest.setText( "Ask For The Driver " );


                                            }
                                        }
                                    } );

                                }
                            }
                        }
                    } );

                }
            }







        } );
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
//        LatLng sydney = new LatLng( -34, 151 );
//        mMap.addMarker( new MarkerOptions().position( sydney ).title( "Marker in Sydney" ) );
//        mMap.moveCamera( CameraUpdateFactory.newLatLng( sydney ) );
//



            //inti location manger
            locationManager = (LocationManager) getSystemService( LOCATION_SERVICE );

            //anonymous inner class for location listener
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    updateCameraPassengerLocation( location );


                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            if(Build.VERSION.SDK_INT<23)
            {
                locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, locationListener );
                Location currentPassengerLocation = locationManager.getLastKnownLocation( LocationManager.GPS_PROVIDER );
                updateCameraPassengerLocation( currentPassengerLocation );

            }


            if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions( new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000 );
            } else {

                locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, locationListener );
                Location currentPassengerLocation = locationManager.getLastKnownLocation( LocationManager.GPS_PROVIDER );
                updateCameraPassengerLocation( currentPassengerLocation );

            }
        }


    private void updateCameraPassengerLocation(Location pLocation)
    {
        LatLng passengerLocation = new LatLng( pLocation.getLatitude(), pLocation.getLongitude() );
        mMap.clear();

        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( passengerLocation, 20 ) );
        mMap.addMarker( new MarkerOptions().position( passengerLocation ).title( "Your Are Here!!!" ) );


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );

        if(requestCode==1000&& permissions[0]== Manifest.permission.ACCESS_FINE_LOCATION&& grantResults[0]==PackageManager.PERMISSION_GRANTED&&grantResults.length>0)
        {

            if(ContextCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION  )!=PackageManager.PERMISSION_GRANTED)
            {
                locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, locationListener );

                Location currentPassengerLocation = locationManager.getLastKnownLocation( LocationManager.GPS_PROVIDER );
                updateCameraPassengerLocation( currentPassengerLocation );
            }

        }
    }
}

/*

   private GoogleMap mMap;

    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_passenger );
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById( R.id.map );
        mapFragment.getMapAsync( this );
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

/*
@Override
public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;

    //inti location manger
    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE  );

    //anonymous inner class for location listener
    locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            updateCameraPassengerLocation(location);


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    } ;


    if(Build.VERSION.SDK_INT>=23&&ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED) {
        requestPermissions( new String[]{ Manifest.permission.ACCESS_FINE_LOCATION } , 1000 );
    }
    else {

        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, locationListener );
        Location currentPassengerLocation = locationManager.getLastKnownLocation( LocationManager.GPS_PROVIDER );
        updateCameraPassengerLocation( currentPassengerLocation );

    }
    // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng( -34, 151 );
//        mMap.addMarker( new MarkerOptions().position( sydney ).title( "Marker in Sydney" ) );
//        mMap.moveCamera( CameraUpdateFactory.newLatLng( sydney ) );
}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );

        if(requestCode==1000&& permissions[0]== Manifest.permission.ACCESS_FINE_LOCATION&& grantResults[0]==PackageManager.PERMISSION_GRANTED&&grantResults.length>0)
        {

            if(ContextCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION  )!=PackageManager.PERMISSION_GRANTED)
            {
                locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, locationListener );

                Location currentPassengerLocation = locationManager.getLastKnownLocation( LocationManager.GPS_PROVIDER );
                updateCameraPassengerLocation( currentPassengerLocation );
            }

        }
    }

    private void updateCameraPassengerLocation(Location pLocation)
    {
        LatLng passengerLocation = new LatLng( pLocation.getLatitude(), pLocation.getLongitude() );
        mMap.clear();
        mMap.addMarker( new MarkerOptions().position( passengerLocation ).title( "Your Are Here!!!" ) );
        mMap.moveCamera( CameraUpdateFactory.newLatLng( passengerLocation ) );

    }








 */
