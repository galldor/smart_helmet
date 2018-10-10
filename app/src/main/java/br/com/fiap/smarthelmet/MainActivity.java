package br.com.fiap.smarthelmet;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity implements ImpactItemFragment.OnListFragmentInteractionListener, TeamFragment.OnFragmentInteractionListener, PlayersFragment.OnFragmentInteractionListener, AccountFragment.OnFragmentInteractionListener {

    private static final String DATABASE_URL = "https://smart-helmet-35a6a.firebaseio.com/number";
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private TeamFragment teamFragment;
    private ImpactItemFragment impactItemFragment;
    private PlayersFragment playersFragment;
    private AccountFragment accountFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        impactItemFragment = ImpactItemFragment.newInstance(1);
        teamFragment = TeamFragment.newInstance("teste1","teste2");
        playersFragment = PlayersFragment.newInstance("teste1","teste2");
        accountFragment = AccountFragment.newInstance("teste1","teste2");
        teamFragment = TeamFragment.newInstance("teste1","teste2");
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReferenceFromUrl(DATABASE_URL);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, teamFragment);
        transaction.commit();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_team:
                    selectedFragment = teamFragment;
                    break;
                case R.id.navigation_players:
                    selectedFragment = playersFragment;
                    break;
                case R.id.navigation_notifications:
                    selectedFragment = impactItemFragment;
                    break;
                case R.id.navigation_account:
                    selectedFragment = accountFragment;
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_container, selectedFragment);
            transaction.commit();
            return true;
        }
    };

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
