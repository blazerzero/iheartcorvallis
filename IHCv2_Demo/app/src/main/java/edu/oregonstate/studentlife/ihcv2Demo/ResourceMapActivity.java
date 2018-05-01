package edu.oregonstate.studentlife.ihcv2Demo;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

import edu.oregonstate.studentlife.ihcv2Demo.data.ResourceMarker;

public class ResourceMapActivity extends FragmentActivity
        {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_map);

        overridePendingTransition(0,0);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

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




}
