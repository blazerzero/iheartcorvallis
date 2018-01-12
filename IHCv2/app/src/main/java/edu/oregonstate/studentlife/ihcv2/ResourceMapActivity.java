package edu.oregonstate.studentlife.ihcv2;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ResourceMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        // Add a marker in Oregon State University and move the camera

        LatLng Corvallis = new LatLng(44.564663, -123.263282);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Corvallis));

        LatLng pioneerPark = new LatLng(44.554949, -123.269763);
        mMap.addMarker(new MarkerOptions().position(pioneerPark).title("Pioneer Park"));

        LatLng AveryPark = new LatLng(44.554001, -123.271747);
        mMap.addMarker(new MarkerOptions().position(AveryPark).title("Avery Park"));

        LatLng AMC = new LatLng(44.588004, -123.249011);
        mMap.addMarker(new MarkerOptions().position(AMC).title("AMC Corvallis 12 (Carmike)"));

        LatLng RegalCinema = new LatLng(44.584979, -123.258667);
        mMap.addMarker(new MarkerOptions().position(RegalCinema).title("Regal Cinema"));

        LatLng OsbornAquaticCenter = new LatLng(44.588113, -123.263179);
        mMap.addMarker(new MarkerOptions().position(OsbornAquaticCenter).title("Osborn Aquatic Center"));

        LatLng Highlandbowl = new LatLng(44.590093, -123.253437);
        mMap.addMarker(new MarkerOptions().position(Highlandbowl).title("Highland bowl"));

        LatLng WithamHill = new LatLng(44.579724, -123.299653);
        mMap.addMarker(new MarkerOptions().position(WithamHill).title("Withm Hill Natural Area"));

        LatLng CristalLake = new LatLng(44.549943, -123.251701);
        mMap.addMarker(new MarkerOptions().position(CristalLake).title("Cristal Lake Sports Field"));

        LatLng DarkSide = new LatLng(44.563333, -123.262436);
        mMap.addMarker(new MarkerOptions().position(DarkSide).title("Darkside Cinema"));

        LatLng Magestic = new LatLng(44.563662, -123.259662);
        mMap.addMarker(new MarkerOptions().position(Magestic).title("Magestic Theater"));

        LatLng McKinleySkate = new LatLng(44.557754, -123.262474);
        mMap.addMarker(new MarkerOptions().position(McKinleySkate).title("McKinley Skate Park"));

        LatLng SafewayDowntown = new LatLng(44.560853, -123.263348);
        mMap.addMarker(new MarkerOptions().position(SafewayDowntown).title("Safeway Downtown"));

        LatLng Fred = new LatLng(44.575261, -123.274158);
        mMap.addMarker(new MarkerOptions().position(Fred).title("Fred Meyer"));

        LatLng GroceryOutlet = new LatLng(44.584359, -123.256133);
        mMap.addMarker(new MarkerOptions().position(GroceryOutlet).title("Grocery Outlet"));

        LatLng NaturalFood = new LatLng(44.553891, -123.264552);
        mMap.addMarker(new MarkerOptions().position(NaturalFood).title("Natural Food Co-Op"));

        LatLng CountryMarket = new LatLng(44.570658, -123.312247);
        mMap.addMarker(new MarkerOptions().position(CountryMarket).title("Country Market & Deli"));

        LatLng BazzarInt = new LatLng(44.543984, -123.266391);
        mMap.addMarker(new MarkerOptions().position(BazzarInt).title("Bazzar International Market"));

        LatLng Safewayphilo = new LatLng(44.550462, -123.308768);
        mMap.addMarker(new MarkerOptions().position(Safewayphilo).title("Safeway Philomath"));

        LatLng BiMart = new LatLng(44.549937, -123.311110);
        mMap.addMarker(new MarkerOptions().position(BiMart).title("Bi-Mart"));

        LatLng Winco = new LatLng(44.590630, -123.274336);
        mMap.addMarker(new MarkerOptions().position(Winco).title("Winco"));

        LatLng AlJebal = new LatLng(44.543992, -123.266417);
        mMap.addMarker(new MarkerOptions().position(AlJebal).title("Al-Jebal"));

        LatLng Flattail = new LatLng(44.562705, -123.259747);
        mMap.addMarker(new MarkerOptions().position(Flattail).title("Flat Tail"));

        LatLng AmericanDream = new LatLng(44.569039, -123.279511);
        mMap.addMarker(new MarkerOptions().position(AmericanDream).title("American Dream Pizza Campus"));

        LatLng Delocias = new LatLng(44.589143, -123.256930);
        mMap.addMarker(new MarkerOptions().position(Delocias).title("Delocias"));

        LatLng Sada = new LatLng(44.564060, -123.259076);
        mMap.addMarker(new MarkerOptions().position(Sada).title("Sada"));

        LatLng Baguette = new LatLng(44.563829, -123.260973);
        mMap.addMarker(new MarkerOptions().position(Baguette).title("Baguette"));

        LatLng Cibellis = new LatLng(44.572909, -123.264290);
        mMap.addMarker(new MarkerOptions().position(Cibellis).title("Cibellis"));

        LatLng Bellhop = new LatLng(44.562878, -123.259998);
        mMap.addMarker(new MarkerOptions().position(Bellhop).title("Bellhop"));

        LatLng Korlander = new LatLng(44.563070, -123.261321);
        mMap.addMarker(new MarkerOptions().position(Korlander).title("Korlander"));

        LatLng Evergreen = new LatLng(44.563768, -123.261402);
        mMap.addMarker(new MarkerOptions().position(Evergreen).title("Evergreen"));

        LatLng LocalBoyz = new LatLng(44.567537, -123.272425);
        mMap.addMarker(new MarkerOptions().position(LocalBoyz).title("LocalBoyz"));

        LatLng NearlyNormals = new LatLng(44.567824, -123.272729);
        mMap.addMarker(new MarkerOptions().position(NearlyNormals).title("Nearly Normals"));

        LatLng DelAlma = new LatLng(44.559705, -123.261257);
        mMap.addMarker(new MarkerOptions().position(DelAlma).title("Del Alma"));

        LatLng Francescos = new LatLng(44.562923, -123.260683);
        mMap.addMarker(new MarkerOptions().position(Francescos).title("Francescos"));

        LatLng Block15 = new LatLng(44.562320, -123.262209);
        mMap.addMarker(new MarkerOptions().position(Block15).title("Block15"));

        LatLng Tommys4th = new LatLng(44.561884, -123.263545);
        mMap.addMarker(new MarkerOptions().position(Tommys4th).title("Tommys4th"));

        LatLng BrokenYoke = new LatLng(44.563961, -123.260942);
        mMap.addMarker(new MarkerOptions().position(BrokenYoke).title("Broken Yoke"));

        LatLng AomatsuShushi = new LatLng(44.564983, -123.260890);
        mMap.addMarker(new MarkerOptions().position(AomatsuShushi).title("Aomatsu Shushi"));

        LatLng LaRockita = new LatLng(44.559458, -123.264392);
        mMap.addMarker(new MarkerOptions().position(LaRockita).title("La Rockita"));

        LatLng QueensChopstick = new LatLng(44.590595, -123.275315);
        mMap.addMarker(new MarkerOptions().position(QueensChopstick).title("Queens Chopstick"));

        LatLng Stones = new LatLng(44.590769, -123.275032);
        mMap.addMarker(new MarkerOptions().position(Stones).title("2 Stones"));

        LatLng MIX = new LatLng(44.550099, -123.310598);
        mMap.addMarker(new MarkerOptions().position(MIX).title("2 Mix"));

        LatLng WackyYo = new LatLng(44.551931, -123.307519);
        mMap.addMarker(new MarkerOptions().position(WackyYo).title("Wacky Yo"));

        LatLng CorvallisMArket = new LatLng(44.582303, -123.259970);
        mMap.addMarker(new MarkerOptions().position(CorvallisMArket).title("Corvallis Market"));

        LatLng Big5 = new LatLng(44.589441, -123.250757);
        mMap.addMarker(new MarkerOptions().position(Big5).title("Big5"));

        LatLng KingsCircle = new LatLng(44.587802, -123.275404);
        mMap.addMarker(new MarkerOptions().position(KingsCircle).title("KingsCircle"));

        LatLng Timberhill = new LatLng(44.590851, -123.274362);
        mMap.addMarker(new MarkerOptions().position(Timberhill).title("Timberhill"));

        LatLng Footwise = new LatLng(44.563652, -123.261626);
        mMap.addMarker(new MarkerOptions().position(Footwise).title("Footwise"));

        LatLng Revolve = new LatLng(44.563862, -123.259677);
        mMap.addMarker(new MarkerOptions().position(Revolve).title("Re*volve"));

        LatLng Bike = new LatLng(44.561208, -123.262332);
        mMap.addMarker(new MarkerOptions().position(Bike).title("Bike"));

        LatLng DollarTree = new LatLng(44.550547, -123.307168);
        mMap.addMarker(new MarkerOptions().position(DollarTree).title("Dollar Tree"));

        LatLng PublicLib = new LatLng(44.565667, -123.264363);
        mMap.addMarker(new MarkerOptions().position(PublicLib).title("Public Libary"));

        LatLng CorvallisCityBuilding = new LatLng(44.565799, -123.263111);
        mMap.addMarker(new MarkerOptions().position(CorvallisCityBuilding).title("Corvallis City Building"));

        LatLng CircuitCourt = new LatLng(44.565303, -123.262365);
        mMap.addMarker(new MarkerOptions().position(CircuitCourt).title("Circuit Court"));

        LatLng BentonCountyHealthCenter = new LatLng(44.573314, -123.281478);
        mMap.addMarker(new MarkerOptions().position(BentonCountyHealthCenter).title("Benton County Health Center"));

        LatLng PostOffice = new LatLng(44.561721, -123.260467);
        mMap.addMarker(new MarkerOptions().position(PostOffice).title("Post Office"));

        LatLng StudentHealthServices = new LatLng(44.567754, -123.278265);
        mMap.addMarker(new MarkerOptions().position(StudentHealthServices).title("Student Health Services"));

        LatLng ValleyLib = new LatLng(44.565328, -123.276003);
        mMap.addMarker(new MarkerOptions().position(ValleyLib).title("Valley Libary"));

        LatLng IntramuralField = new LatLng(44.562773, -123.280795);
        mMap.addMarker(new MarkerOptions().position(IntramuralField).title("Intramural Field"));

        LatLng BeaverStore = new LatLng(44.561334, -123.279497);
        mMap.addMarker(new MarkerOptions().position(BeaverStore).title("Beaver Store"));

        LatLng Reser = new LatLng(44.559607, -123.281422);
        mMap.addMarker(new MarkerOptions().position(Reser).title("Reser"));

        LatLng Dixon = new LatLng(44.563168, -123.278617);
        mMap.addMarker(new MarkerOptions().position(Dixon).title("Dixon"));

        LatLng MemorialUnion = new LatLng(44.565031, -123.278900);
        mMap.addMarker(new MarkerOptions().position(MemorialUnion).title("Memorial Union"));

        LatLng CorvallisCommunityRelationOffice = new LatLng(44.564236, -123.276596);
        mMap.addMarker(new MarkerOptions().position(CorvallisCommunityRelationOffice).title("Corvallis Community Relation Office"));

        LatLng PeavyFeilds = new LatLng(44.565122, -123.286945);
        mMap.addMarker(new MarkerOptions().position(PeavyFeilds).title("Peavy Feilds"));


    }
}
