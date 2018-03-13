package edu.oregonstate.studentlife.ihcv2;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import edu.oregonstate.studentlife.ihcv2.loaders.MarkerLoader;

public class ResourceMapActivity extends FragmentActivity
        implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<String> {

    private GoogleMap mMap;

    private final static String TAG = ResourceMapActivity.class.getSimpleName();
    private final static int IHC_MARKER_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_map);

        overridePendingTransition(0,0);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
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
        mMap.setMinZoomPreference(12.0f);

        // Set the camera over Corvallis
        LatLng Corvallis = new LatLng(44.564663, -123.263282);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Corvallis));

        //new ResourceMarkerReceiver(this).execute();
        getSupportLoaderManager().initLoader(IHC_MARKER_LOADER_ID, null, this);

    }

    public void newMarker(LatLng latLng, String name) {
        mMap.addMarker(new MarkerOptions().position(latLng).title(name));
    }

    /*private void onBackgroundTaskDataObtained(String result) {
        StringTokenizer stMarker = new StringTokenizer(result, "\\");
        while (stMarker.hasMoreTokens()) {
            try {
                String markerString = stMarker.nextToken();
                JSONObject markerJSON = new JSONObject(markerString);
                String markerName = markerJSON.getString("name");
                String markerAddress = markerJSON.getString("address");
                String markerType = markerJSON.getString("type");

                Geocoder coder = new Geocoder(this);
                List<Address> address;
                LatLng markerLatLng = null;

                address = coder.getFromLocationName(markerAddress, 5);
                if (address == null || address.size() == 0) {
                    Log.d(TAG, "Error geocoding address.");
                } else {
                    Address location = address.get(0);
                    markerLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    if (markerType.equals("Activities and Entertainment")) {
                        mMap.addMarker(new MarkerOptions()
                                .position(markerLatLng)
                                .title(markerName)
                                .icon(BitmapDescriptorFactory.defaultMarker(210))); // blue
                    } else if (markerType.equals("Grocery Stores")) {
                        mMap.addMarker(new MarkerOptions()
                                .position(markerLatLng)
                                .title(markerName)
                                .icon(BitmapDescriptorFactory.defaultMarker(120))); // green
                    } else if (markerType.equals("Restaurants")) {
                        mMap.addMarker(new MarkerOptions()
                                .position(markerLatLng)
                                .title(markerName)
                                .icon(BitmapDescriptorFactory.defaultMarker(0)));   // red
                    } else if (markerType.equals("Shopping")) {
                        mMap.addMarker(new MarkerOptions()
                                .position(markerLatLng)
                                .title(markerName)
                                .icon(BitmapDescriptorFactory.defaultMarker(270))); // purple
                    } else if (markerType.equals("City Offices")) {
                        mMap.addMarker(new MarkerOptions()
                                .position(markerLatLng)
                                .title(markerName)
                                .icon(BitmapDescriptorFactory.defaultMarker(60)));  // yellow
                    } else if (markerType.equals("OSU Campus")) {
                        mMap.addMarker(new MarkerOptions()
                                .position(markerLatLng)
                                .title(markerName)
                                .icon(BitmapDescriptorFactory.defaultMarker(30)));  // orange
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }*/

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new MarkerLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d(TAG, "got markers from loader");
        try {
            StringTokenizer stMarker = new StringTokenizer(data, "\\");
            while (stMarker.hasMoreTokens()) {
                String markerString = stMarker.nextToken();
                JSONObject markerJSON = new JSONObject(markerString);
                String markerName = markerJSON.getString("name");
                String markerAddress = markerJSON.getString("address");
                String markerType = markerJSON.getString("type");

                Geocoder coder = new Geocoder(this);
                List<Address> address;
                LatLng markerLatLng = null;

                address = coder.getFromLocationName(markerAddress, 5);
                if (address == null || address.size() == 0) {
                    Log.d(TAG, "Error geocoding address.");
                } else {
                    Address location = address.get(0);
                    markerLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    if (markerType.equals("Activities and Entertainment")) {
                        mMap.addMarker(new MarkerOptions()
                                .position(markerLatLng)
                                .title(markerName)
                                .icon(BitmapDescriptorFactory.defaultMarker(210))); // blue
                    } else if (markerType.equals("Grocery Stores")) {
                        mMap.addMarker(new MarkerOptions()
                                .position(markerLatLng)
                                .title(markerName)
                                .icon(BitmapDescriptorFactory.defaultMarker(120))); // green
                    } else if (markerType.equals("Restaurants")) {
                        mMap.addMarker(new MarkerOptions()
                                .position(markerLatLng)
                                .title(markerName)
                                .icon(BitmapDescriptorFactory.defaultMarker(0)));   // red
                    } else if (markerType.equals("Shopping")) {
                        mMap.addMarker(new MarkerOptions()
                                .position(markerLatLng)
                                .title(markerName)
                                .icon(BitmapDescriptorFactory.defaultMarker(270))); // purple
                    } else if (markerType.equals("City Offices")) {
                        mMap.addMarker(new MarkerOptions()
                                .position(markerLatLng)
                                .title(markerName)
                                .icon(BitmapDescriptorFactory.defaultMarker(60)));  // yellow
                    } else if (markerType.equals("OSU Campus")) {
                        mMap.addMarker(new MarkerOptions()
                                .position(markerLatLng)
                                .title(markerName)
                                .icon(BitmapDescriptorFactory.defaultMarker(30)));  // orange
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // Nothing to do...
    }

    /*class ResourceMarkerReceiver extends AsyncTask {

        private Context context;
        final static String IHC_GET_RESOURCE_MARKERS_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/getresource_markers.php";

        public ResourceMarkerReceiver(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            try {
                URL url = new URL(IHC_GET_RESOURCE_MARKERS_URL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuffer sb = new StringBuffer("");
                String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                return sb.toString();
            } catch (Exception e) { return new String("Exception: " + e.getMessage()); }
        }


        @Override
        protected void onPostExecute(Object result) {
            String resultString = (String) result;
            ResourceMapActivity.this.onBackgroundTaskDataObtained(resultString);
        }
    }*/
}
