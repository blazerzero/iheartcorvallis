package edu.oregonstate.studentlife.ihcv2;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Vector;

public class ResourceMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

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

        // Add markers for resource locations

        /* ACTIVITIES AND ENTERTAINMENT */
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

        LatLng HighlandBowl = new LatLng(44.590093, -123.253437);
        mMap.addMarker(new MarkerOptions().position(HighlandBowl).title("Highland Bowl"));

        LatLng WithamHill = new LatLng(44.579724, -123.299653);
        mMap.addMarker(new MarkerOptions().position(WithamHill).title("Witham Hill Natural Area"));

        LatLng CrystalLake = new LatLng(44.549943, -123.251701);
        mMap.addMarker(new MarkerOptions().position(CrystalLake).title("Crystal Lake Sports Field"));

        LatLng DarkSide = new LatLng(44.563333, -123.262436);
        mMap.addMarker(new MarkerOptions().position(DarkSide).title("Darkside Cinema"));

        LatLng Magestic = new LatLng(44.563662, -123.259662);
        mMap.addMarker(new MarkerOptions().position(Magestic).title("Magestic Theatre"));

        LatLng McKinleySkate = new LatLng(44.557754, -123.262474);
        mMap.addMarker(new MarkerOptions().position(McKinleySkate).title("McKinley Skate Park"));

        /* GROCERY STORES */
        LatLng SafewayDowntown = new LatLng(44.560853, -123.263348);
        mMap.addMarker(new MarkerOptions().position(SafewayDowntown).title("Safeway (Downtown)"));

        LatLng FredMeyer = new LatLng(44.575261, -123.274158);
        mMap.addMarker(new MarkerOptions().position(FredMeyer).title("Fred Meyer"));

        LatLng GroceryOutlet = new LatLng(44.584359, -123.256133);
        mMap.addMarker(new MarkerOptions().position(GroceryOutlet).title("Grocery Outlet"));

        LatLng NaturalFood = new LatLng(44.553891, -123.264552);
        mMap.addMarker(new MarkerOptions().position(NaturalFood).title("Natural Food Co-Op"));

        LatLng CountryMarket = new LatLng(44.570658, -123.312247);
        mMap.addMarker(new MarkerOptions().position(CountryMarket).title("Country Market and Deli"));

        LatLng BazzarInt = new LatLng(44.543984, -123.266391);
        mMap.addMarker(new MarkerOptions().position(BazzarInt).title("Bazzar International Market"));

        LatLng Safewayphilo = new LatLng(44.550462, -123.308768);
        mMap.addMarker(new MarkerOptions().position(Safewayphilo).title("Safeway (Philomath)"));

        LatLng BiMart = new LatLng(44.549937, -123.311110);
        mMap.addMarker(new MarkerOptions().position(BiMart).title("Bi-Mart"));

        LatLng WinCo = new LatLng(44.590630, -123.274336);
        mMap.addMarker(new MarkerOptions().position(WinCo).title("WinCo"));

        /* RESTAURANTS */
        LatLng AlJebal = new LatLng(44.543992, -123.266417);
        mMap.addMarker(new MarkerOptions().position(AlJebal).title("Al-Jebal Middle-Eastern Restaurant"));

        LatLng FlatTail = new LatLng(44.562705, -123.259747);
        mMap.addMarker(new MarkerOptions().position(FlatTail).title("Flat Tail Brewing"));

        LatLng AmericanDream = new LatLng(44.569039, -123.279511);
        mMap.addMarker(new MarkerOptions().position(AmericanDream).title("American Dream Pizza (Campus)"));

        LatLng Delicias = new LatLng(44.589143, -123.256930);
        mMap.addMarker(new MarkerOptions().position(Delicias).title("Delicias Valley Cafe"));

        LatLng Sada = new LatLng(44.564060, -123.259076);
        mMap.addMarker(new MarkerOptions().position(Sada).title("Sada Sushi & Izakaya"));

        LatLng Baguette = new LatLng(44.563829, -123.260973);
        mMap.addMarker(new MarkerOptions().position(Baguette).title("Baguette Vietnamese Sandwiches"));

        LatLng Cibellis = new LatLng(44.572909, -123.264290);
        mMap.addMarker(new MarkerOptions().position(Cibellis).title("Cibelli's Pizza"));

        LatLng Bellhop = new LatLng(44.562878, -123.259998);
        mMap.addMarker(new MarkerOptions().position(Bellhop).title("BELLHOP | Brothers in Cheer"));

        LatLng Koriander = new LatLng(44.563070, -123.261321);
        mMap.addMarker(new MarkerOptions().position(Koriander).title("Koriander"));

        LatLng Evergreen = new LatLng(44.563768, -123.261402);
        mMap.addMarker(new MarkerOptions().position(Evergreen).title("Evergreen Indian Restaurant"));

        LatLng LocalBoyz = new LatLng(44.567537, -123.272425);
        mMap.addMarker(new MarkerOptions().position(LocalBoyz).title("Local Boyz Hawaiian Cafe"));

        LatLng NearlyNormals = new LatLng(44.567824, -123.272729);
        mMap.addMarker(new MarkerOptions().position(NearlyNormals).title("Nearly Normals Gonzo Cuisine"));

        LatLng DelAlma = new LatLng(44.559705, -123.261257);
        mMap.addMarker(new MarkerOptions().position(DelAlma).title("del Alma Restaurant"));

        LatLng Francescos = new LatLng(44.562923, -123.260683);
        mMap.addMarker(new MarkerOptions().position(Francescos).title("Francesco's"));

        LatLng Block15 = new LatLng(44.562320, -123.262209);
        mMap.addMarker(new MarkerOptions().position(Block15).title("Block 15 Brewing Co"));

        LatLng Tommys4th = new LatLng(44.561884, -123.263545);
        mMap.addMarker(new MarkerOptions().position(Tommys4th).title("Tommy's 4th Street Bar & Grill"));

        LatLng BrokenYolk = new LatLng(44.563961, -123.260942);
        mMap.addMarker(new MarkerOptions().position(BrokenYolk).title("Broken Yolk Cafe"));

        LatLng AomatsuSushi = new LatLng(44.564983, -123.260890);
        mMap.addMarker(new MarkerOptions().position(AomatsuSushi).title("Aomatsu Sushi & Grill"));

        LatLng LaRockita = new LatLng(44.559458, -123.264392);
        mMap.addMarker(new MarkerOptions().position(LaRockita).title("La Rockita Downtown"));

        LatLng QueensChopstick = new LatLng(44.590595, -123.275315);
        mMap.addMarker(new MarkerOptions().position(QueensChopstick).title("Queen's Chopstick"));

        LatLng Stones = new LatLng(44.590769, -123.275032);
        mMap.addMarker(new MarkerOptions().position(Stones).title("2 Stones Wood Fired Italian Trattoria"));

        LatLng MIX = new LatLng(44.550099, -123.310598);
        mMap.addMarker(new MarkerOptions().position(MIX).title("2MIX food & beverages"));

        LatLng WackyYo = new LatLng(44.551931, -123.307519);
        mMap.addMarker(new MarkerOptions().position(WackyYo).title("Wacky Yo"));

        /* SHOPPING */
        LatLng CorvallisMarket = new LatLng(44.582303, -123.259970);
        mMap.addMarker(new MarkerOptions().position(CorvallisMarket).title("Corvallis Market Center"));

        LatLng Big5 = new LatLng(44.589441, -123.250757);
        mMap.addMarker(new MarkerOptions().position(Big5).title("Big 5 Sporting Goods"));

        LatLng KingsCircle = new LatLng(44.587802, -123.275404);
        mMap.addMarker(new MarkerOptions().position(KingsCircle).title("Kings Circle Shopping Center"));

        LatLng Timberhill = new LatLng(44.590851, -123.274362);
        mMap.addMarker(new MarkerOptions().position(Timberhill).title("Timberhill Shopping Center"));

        LatLng Footwise = new LatLng(44.563652, -123.261626);
        mMap.addMarker(new MarkerOptions().position(Footwise).title("Footwise in Corvallis"));

        LatLng Revolve = new LatLng(44.563862, -123.259677);
        mMap.addMarker(new MarkerOptions().position(Revolve).title("re*volve"));

        LatLng BikeNHike = new LatLng(44.561208, -123.262332);
        mMap.addMarker(new MarkerOptions().position(BikeNHike).title("Bike N Hike"));

        LatLng DollarTree = new LatLng(44.550547, -123.307168);
        mMap.addMarker(new MarkerOptions().position(DollarTree).title("Dollar Tree"));

        /* CITY OFFICES */
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

        /* OSU CAMPUS */
        LatLng StudentHealthServices = new LatLng(44.567754, -123.278265);
        mMap.addMarker(new MarkerOptions().position(StudentHealthServices).title("Student Health Services"));

        LatLng ValleyLib = new LatLng(44.565328, -123.276003);
        mMap.addMarker(new MarkerOptions().position(ValleyLib).title("The Valley Libary"));

        LatLng IntramuralField = new LatLng(44.562773, -123.280795);
        mMap.addMarker(new MarkerOptions().position(IntramuralField).title("Intramural Field"));

        LatLng BeaverStore = new LatLng(44.561334, -123.279497);
        mMap.addMarker(new MarkerOptions().position(BeaverStore).title("Beaver Store"));

        LatLng Reser = new LatLng(44.559607, -123.281422);
        mMap.addMarker(new MarkerOptions().position(Reser).title("Reser Stadium"));

        LatLng Dixon = new LatLng(44.563168, -123.278617);
        mMap.addMarker(new MarkerOptions().position(Dixon).title("Dixon Rec Center"));

        LatLng MemorialUnion = new LatLng(44.565031, -123.278900);
        mMap.addMarker(new MarkerOptions().position(MemorialUnion).title("Memorial Union"));

        LatLng CorvallisCommunityRelationOffice = new LatLng(44.564236, -123.276596);
        mMap.addMarker(new MarkerOptions().position(CorvallisCommunityRelationOffice).title("Corvallis Community Relation Office"));

        LatLng PeavyFields = new LatLng(44.565122, -123.286945);
        mMap.addMarker(new MarkerOptions().position(PeavyFields).title("Peavy Fields"));

    }

    public void newMarker(LatLng latLng, String name) {
        mMap.addMarker(new MarkerOptions().position(latLng).title(name));
    }
}
