package edu.oregonstate.studentlife.ihcv2;

import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import edu.oregonstate.studentlife.ihcv2.data.ResourceMarker;
import edu.oregonstate.studentlife.ihcv2.loaders.MarkerLoader;

/**
 * Uses the Google Maps API and the MarkerLoader to populate a google map with available notable
 * resources in Corvallis, such as markets and city centers.
 */

public class ResourceMapActivity extends FragmentActivity
        implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<String> {

    private GoogleMap mMap;
    private TextView legendTitleTV;
    private TextView activitiesEntertainmentTV;
    private TextView groceryStoresTV;
    private TextView restaurantsTV;
    private TextView shoppingTV;
    private TextView cityOfficesTV;
    private TextView osuCampusTV;
    private ArrayList<ResourceMarker> resourceMarkerList;

    private boolean isLegendOpen = false;
    private boolean actEntOnly = false;
    private boolean groceryOnly = false;
    private boolean restaurantOnly = false;
    private boolean shoppingOnly = false;
    private boolean cityOfficesOnly = false;
    private boolean osuCampusOnly = false;


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

        legendTitleTV = (TextView) findViewById(R.id.gm_legend_title);
        activitiesEntertainmentTV = (TextView) findViewById(R.id.gm_legend_activities);
        groceryStoresTV = (TextView) findViewById(R.id.gm_legend_grocery);
        restaurantsTV = (TextView) findViewById(R.id.gm_legend_restaurants);
        shoppingTV = (TextView) findViewById(R.id.gm_legend_shopping);
        cityOfficesTV = (TextView) findViewById(R.id.gm_legend_cityoffices);
        osuCampusTV = (TextView) findViewById(R.id.gm_legend_osucampus);

        legendTitleTV.getBackground().setAlpha(191);
        activitiesEntertainmentTV.getBackground().setAlpha(191);
        groceryStoresTV.getBackground().setAlpha(191);
        restaurantsTV.getBackground().setAlpha(191);
        shoppingTV.getBackground().setAlpha(191);
        cityOfficesTV.getBackground().setAlpha(191);
        osuCampusTV.getBackground().setAlpha(191);

        resourceMarkerList = new ArrayList<ResourceMarker>();

        legendTitleTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLegendOpen) {
                    activitiesEntertainmentTV.setVisibility(View.GONE);
                    groceryStoresTV.setVisibility(View.GONE);
                    restaurantsTV.setVisibility(View.GONE);
                    shoppingTV.setVisibility(View.GONE);
                    cityOfficesTV.setVisibility(View.GONE);
                    osuCampusTV.setVisibility(View.GONE);
                    isLegendOpen = false;
                }
                else {
                    activitiesEntertainmentTV.setVisibility(View.VISIBLE);
                    groceryStoresTV.setVisibility(View.VISIBLE);
                    restaurantsTV.setVisibility(View.VISIBLE);
                    shoppingTV.setVisibility(View.VISIBLE);
                    cityOfficesTV.setVisibility(View.VISIBLE);
                    osuCampusTV.setVisibility(View.VISIBLE);
                    isLegendOpen = true;
                }
            }
        });

        activitiesEntertainmentTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!actEntOnly) {
                    for (ResourceMarker marker : resourceMarkerList) {
                        if (!marker.getType().equals("Activities and Entertainment")) {
                            marker.setMarkerVisibility(false);
                        } else {
                            marker.setMarkerVisibility(true);
                        }
                    }
                    actEntOnly = true;
                }
                else {
                    showAllMarkers();
                }
            }
        });

        groceryStoresTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!groceryOnly) {
                    for (ResourceMarker marker : resourceMarkerList) {
                        if (!marker.getType().equals("Grocery Stores")) {
                            marker.setMarkerVisibility(false);
                        } else {
                            marker.setMarkerVisibility(true);
                        }
                    }
                    groceryOnly = true;
                }
                else {
                    showAllMarkers();
                }
            }
        });

        restaurantsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!restaurantOnly) {
                    for (ResourceMarker marker : resourceMarkerList) {
                        if (!marker.getType().equals("Restaurants")) {
                            marker.setMarkerVisibility(false);
                        } else {
                            marker.setMarkerVisibility(true);
                        }
                    }
                    restaurantOnly = true;
                }
                else {
                    showAllMarkers();
                }
            }
        });

        shoppingTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!shoppingOnly) {
                    for (ResourceMarker marker : resourceMarkerList) {
                        if (!marker.getType().equals("Shopping")) {
                            marker.setMarkerVisibility(false);
                        } else {
                            marker.setMarkerVisibility(true);
                        }
                    }
                    shoppingOnly = true;
                }
                else {
                    showAllMarkers();
                }
            }
        });

        cityOfficesTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cityOfficesOnly) {
                    for (ResourceMarker marker : resourceMarkerList) {
                        if (!marker.getType().equals("City Offices")) {
                            marker.setMarkerVisibility(false);
                        } else {
                            marker.setMarkerVisibility(true);
                        }
                    }
                    cityOfficesOnly = true;
                }
                else {
                    showAllMarkers();
                }
            }
        });

        osuCampusTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!osuCampusOnly) {
                    for (ResourceMarker marker : resourceMarkerList) {
                        if (!marker.getType().equals("OSU Campus")) {
                            marker.setMarkerVisibility(false);
                        } else {
                            marker.setMarkerVisibility(true);
                        }
                    }
                    osuCampusOnly = true;
                }
                else {
                    showAllMarkers();
                }
            }
        });
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

        getSupportLoaderManager().initLoader(IHC_MARKER_LOADER_ID, null, this);
    }

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
                Marker marker;
                LatLng markerLatLng = null;

                address = coder.getFromLocationName(markerAddress, 5);
                if (address == null || address.size() == 0) {
                    Log.d(TAG, "Error geocoding address.");
                } else {
                    Address location = address.get(0);
                    markerLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    if (markerType.equals("Activities and Entertainment")) {
                        marker = mMap.addMarker(new MarkerOptions()
                                .position(markerLatLng)
                                .title(markerName)
                                .icon(BitmapDescriptorFactory.defaultMarker(210))); // blue
                    } else if (markerType.equals("Grocery Stores")) {
                        marker = mMap.addMarker(new MarkerOptions()
                                .position(markerLatLng)
                                .title(markerName)
                                .icon(BitmapDescriptorFactory.defaultMarker(120))); // green
                    } else if (markerType.equals("Restaurants")) {
                        marker = mMap.addMarker(new MarkerOptions()
                                .position(markerLatLng)
                                .title(markerName)
                                .icon(BitmapDescriptorFactory.defaultMarker(0)));   // red
                    } else if (markerType.equals("Shopping")) {
                        marker = mMap.addMarker(new MarkerOptions()
                                .position(markerLatLng)
                                .title(markerName)
                                .icon(BitmapDescriptorFactory.defaultMarker(270))); // purple
                    } else if (markerType.equals("City Offices")) {
                        marker = mMap.addMarker(new MarkerOptions()
                                .position(markerLatLng)
                                .title(markerName)
                                .icon(BitmapDescriptorFactory.defaultMarker(60)));  // yellow
                    } else if (markerType.equals("OSU Campus")) {
                        marker = mMap.addMarker(new MarkerOptions()
                                .position(markerLatLng)
                                .title(markerName)
                                .icon(BitmapDescriptorFactory.defaultMarker(30)));  // orange
                    } else {
                        marker = null;
                    }
                    ResourceMarker resourceMarker = new ResourceMarker(markerName, location, markerLatLng, markerType, marker);
                    resourceMarkerList.add(resourceMarker);
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

    private void showAllMarkers() {
        for (ResourceMarker marker : resourceMarkerList) {
            marker.setMarkerVisibility(true);
        }
        actEntOnly = false;
        groceryOnly = false;
        restaurantOnly = false;
        shoppingOnly = false;
        cityOfficesOnly = false;
        osuCampusOnly = false;
    }

}
