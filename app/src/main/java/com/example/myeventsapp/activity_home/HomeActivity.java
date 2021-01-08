package com.example.myeventsapp.activity_home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.myeventsapp.R;
import com.example.myeventsapp.home_activity.bnb_fragments.HomeFragment;
import com.example.myeventsapp.home_activity.bnb_fragments.NewEventFragment;
import com.example.myeventsapp.home_activity.bnb_fragments.NotificationFragment;
import com.example.myeventsapp.home_activity.bnb_fragments.ProfileFragment;
import com.example.myeventsapp.home_activity.bnb_fragments.TimelineFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    // Log Tag
    public final String LOG_TAG="context(HomeActivity)";

    // Firebase Auth Objects
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    // UI elements
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /* SET UP UI ELEMENTS */
        // configure BNB
        bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        // implemented BottomNavigationView.OnNavigationItemSelectedListener for adding listener
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        // firstly load HomeFragment as default opened fragment for BNB
        switchToFragment(new HomeFragment());

        /* CONFIGURE FIREBASE AUTH */
        // instantiate firebase auth instance
        mAuth=FirebaseAuth.getInstance();
        // configure request for Google Sign In
        createRequest();
    }

    private void createRequest() {
        // build GoogleSignInOption using oAuth Client ID
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.oAuth_client_id))    // providing oAuth Client ID
                .requestEmail() // requesting Email to be selected
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);    // Creating googleSignInClient from options specified in gso

    }



    // this method inflates activity menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // get menu inflator for activity
        MenuInflater menuInflater=getMenuInflater();
        // infate the home_activity_menu in the menu provider
        menuInflater.inflate(R.menu.home_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_menu_item: // if logout button is pressed
                signOut();  // sign out
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {
        mAuth.signOut();    // signout the user from FirebaseAuth
        // but signing out from FirebaseAuth is not enough for Google Sign in
        // we also need to signout the user using Google Sign in Client
        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();   // close this activity once user signout, this will take to calling activity i.e., LoginActivity
            }
        });

    }

    boolean switchToFragment(Fragment fragment) {
        if(fragment!=null) {
            // replace the fragment using fragment manager
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragments_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // switch to fragment depending on BNB item selected
        Fragment fragmentToLoad=null;
        switch (item.getItemId()){
            case R.id.home:
                fragmentToLoad=new HomeFragment();
                break;
            case R.id.timeline:
                fragmentToLoad=new TimelineFragment();
                break;
            case R.id.add:
                fragmentToLoad=new NewEventFragment();
                break;
            case R.id.notifications:
                fragmentToLoad=new NotificationFragment();
                break;
            case R.id.profile:
                fragmentToLoad=new ProfileFragment();
                break;
        }
        // load the fragment on screen
        return switchToFragment(fragmentToLoad);
    }
}