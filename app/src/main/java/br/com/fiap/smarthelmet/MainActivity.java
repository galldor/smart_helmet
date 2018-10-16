package br.com.fiap.smarthelmet;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity implements ImpactItemFragment.OnListFragmentInteractionListener, TeamFragment.OnFragmentInteractionListener, PlayersFragment.OnFragmentInteractionListener, GraphFragment.OnFragmentInteractionListener {

    private static final String DATABASE_URL = "https://smart-helmet-35a6a.firebaseio.com/number";
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private HashMap<String, Double> hashFirebaseValues;
    private static String TAG = "MainActivityFirebase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReferenceFromUrl(DATABASE_URL);
        hashFirebaseValues = new HashMap<>();


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, TeamFragment.newInstance("teste1","teste2"), "TeamFragment");
        transaction.commit();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        initFirebaseListener();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            String tag = "";
            switch (item.getItemId()) {
                case R.id.navigation_team:
                    tag = "TeamFragment";
                    selectedFragment = TeamFragment.newInstance("teste1","teste2");
                    break;
                case R.id.navigation_players:
                    tag = "PlayersFragment";
                    selectedFragment = PlayersFragment.newInstance("teste1","teste2");
                    break;
                case R.id.navigation_notifications:
                    tag = "ImpactItemFragment";
                    selectedFragment = ImpactItemFragment.newInstance(1);;
                    break;
                case R.id.navigation_account:
                    tag = "GraphFragment";
                    selectedFragment = GraphFragment.newInstance("teste1","teste2");
                    break;
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Fragment savedFragment = fragmentManager.findFragmentByTag(tag);
            transaction.replace(R.id.main_container, savedFragment != null ? savedFragment : selectedFragment, tag);

            transaction.commit();
            return true;
        }
    };

    private void initFirebaseListener(){
        databaseReference = getDatabaseReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Double impactValue = dataSnapshot.getValue(Double.class);
                Log.d(TAG, "value is: " + dataSnapshot.getValue(Double.class));
                Double lastValue = getFirabaseValues().isEmpty() ? 0 : getFirabaseValues().get(getFirabaseValues().size() - 1);
                if(impactValue > 1.0 && lastValue != impactValue) {
                    Date currentTime = Calendar.getInstance().getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                    String currentDateTimeString = sdf.format(currentTime);
                    getFirabaseHashValues().put(currentDateTimeString, impactValue);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public List<Double> getFirabaseValues(){
        return new ArrayList<Double>(hashFirebaseValues.values());
    }

    public HashMap<String, Double> getFirabaseHashValues(){
        return hashFirebaseValues;
    }

    @Override
    public void onListFragmentInteraction(ImpactItem item) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public DatabaseReference getDatabaseReference(){
        return this.databaseReference;
    }
}
