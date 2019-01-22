package saeed.example.com.map;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class DriverActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    //declare the Button, ListView, ArrayAdapter, ArrayList

    private Button btnDiverRequest;
    private ArrayAdapter arrayAdapter;
    private ArrayList arrayList;
    private ArrayList<Double> arrayListLatitude;
    private ArrayList<Double> arrayListLongitude;
    private ListView listView;
    private LocationManager locationManager;
    private LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_driver );
//
//        //init
//
        btnDiverRequest = findViewById( R.id.btnDriverRequest );
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter( this, android.R.layout.simple_list_item_1, arrayList );

        listView = findViewById( R.id.lstView );

        listView.setAdapter( arrayAdapter );

        arrayListLatitude = new ArrayList();

        arrayListLongitude = new ArrayList();


        btnDiverRequest.setOnClickListener( this );


        locationManager = (LocationManager) getSystemService( LOCATION_SERVICE );

        listView.setOnItemClickListener( this );
//
//
////        locationListener = new   LocationListener() {
////            @Override
////            public void onLocationChanged(Location location) {
////                updateLocation( location );
////            }
////
////            @Override
////            public void onStatusChanged(String provider, int status, Bundle extras) {
////
////            }
////
////            @Override
////            public void onProviderEnabled(String provider) {
////
////            }
////
////            @Override
////            public void onProviderDisabled(String provider) {
////
////            }
////        };
//////
//
////        locationManager = (LocationManager) getSystemService( LOCATION_SERVICE );
//
//
////        arrayList.clear();
//
////        arrayAdapter.notifyDataSetChanged();
////
////        try {
////
        if (Build.VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, locationListener );

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
////
////                //        locationListener = new   LocationListener() {
////
////
////            }
////        }
////        catch (Exception e)
////        {
////            e.printStackTrace();
////        }
////
//////
////            try {
////
//////            if (Build.VERSION.SDK_INT < 23) {
//////                locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, locationListener );
//////                Location currentLocation = locationManager.getLastKnownLocation( LocationManager.GPS_PROVIDER );
//////                updateLocation( currentLocation );
//////
//////            }
////
//////
//////                if (Build.VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= 23) {
//////                    requestPermissions( new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100 );
//////
//////                } else {
//////
//////                    locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, locationListener );
//////                    Location currentLocation = locationManager.getLastKnownLocation( LocationManager.GPS_PROVIDER );
//////
//////                }
////
////
////
////
////
////        }catch(Exception e)
////        {
////            e.printStackTrace();
////        }
//
////        try {
//        Log.i( "locationListenerCurrentValue", locationListener.toString() );
////        }
////        catch (Exception e)
////        {
//
////        }
//
//    }

        }
    }


//










    @Override
    public void onClick(View v) {




// transfer the location manager to the onCreate method

//    locationManager = (LocationManager) getSystemService( LOCATION_SERVICE );



 // transfer location listener to the onCreate method
//    locationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(Location location) {
//            updateLocation( location );
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//
//        }
//    };

        //transfer the permission to onCreate method

    if(Build.VERSION.SDK_INT< 23)

        {
            locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, locationListener );
            Location currentLocation = locationManager.getLastKnownLocation( LocationManager.GPS_PROVIDER );
            updateLocation( currentLocation );

        }


    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION )!=PackageManager.PERMISSION_GRANTED &&Build.VERSION.SDK_INT >=23)

        {
            requestPermissions( new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100 );

        } else

        {

            locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, locationListener );
            Location currentLocation = locationManager.getLastKnownLocation( LocationManager.GPS_PROVIDER );
            updateLocation( currentLocation );


        }


//
//    //get the permission
//

    }


    public  void updateLocation(final Location location)
    {




    if (location != null) {



            final ProgressDialog progressDialog = new ProgressDialog( this );
            progressDialog.setMessage( "loading" );
            progressDialog.show();

//


//            LatLng latLng = new LatLng( location.getLatitude(), location.getLongitude() );

        final ParseGeoPoint driverGeoPoint = new ParseGeoPoint( location.getLatitude(), location.getLongitude() );


        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery( "requestCar" );
        parseQuery.whereNear( "location", driverGeoPoint );
        parseQuery.findInBackground( new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {

                    if (arrayList.size() > 0) {
                        arrayList.clear();
                    }

                    for (ParseObject parseObject : objects) {



                            ParseGeoPoint passengersGeoPoint = (ParseGeoPoint) parseObject.get( "location" );


//                      Double distance =  driverGeoPoint.distanceInKilometersTo( (ParseGeoPoint) parseObject.get( "location" ) );
//                      //init parseGeo point
//                       Location passengerLocation = (Location) parseObject.get( "location" );

                            Double distance = driverGeoPoint.distanceInKilometersTo( passengersGeoPoint );
//
//                       arrayListLatitude.add(passengerLocation.getLatitude());
//                       arrayListLongitude.add(passengerLocation.getLongitude());

                            float roundedDistance = Math.round( distance * 10000 ) / 10000f;

                            arrayList.add( "there are " + distance + " km to " + parseObject.get( "username" ) );

                            arrayListLatitude.add( passengersGeoPoint.getLatitude() );
                            arrayListLongitude.add( passengersGeoPoint.getLongitude() );
                        }


                    } else{
                        Toast.makeText( DriverActivity.this, "not gonna happen", Toast.LENGTH_LONG ).show();
                    }
                    progressDialog.dismiss();




                arrayAdapter.notifyDataSetChanged();


            }


        } );
    }


        locationManager.removeUpdates( locationListener );






    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );

        if(requestCode==100&&permissions[0]==Manifest.permission.ACCESS_FINE_LOCATION&&grantResults.length>0)
        {
            locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0,0, locationListener );
            Location currentLocation =locationManager.getLastKnownLocation( LocationManager.GPS_PROVIDER );
           updateLocation( currentLocation );

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent( this, ViewLocations.class );


        //create a location variable to save the driver location
        Location location = locationManager.getLastKnownLocation( LocationManager.GPS_PROVIDER );

        intent.putExtra( "driverLatitude", location.getLatitude() );
        intent.putExtra( "driverLongitude", location.getLongitude() );

        intent.putExtra( "passengerLatitude", arrayListLatitude.get( position ));
        intent.putExtra( "passengerLongitutde", arrayListLongitude.get( position ) );




        startActivity( intent );

    }
//
    }


