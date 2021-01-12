package com.teamsar.eventure.activity_home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.teamsar.eventure.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.teamsar.eventure.activity_addnewevent.AddNewEvent;
import com.teamsar.eventure.activity_home.fragments_bnb.HomeFragment;
import com.teamsar.eventure.activity_home.fragments_bnb.NotificationFragment;
import com.teamsar.eventure.activity_home.fragments_bnb.ProfileFragment;
import com.teamsar.eventure.activity_home.fragments_bnb.TimelineFragment;
import com.teamsar.eventure.activity_login.LoginActivity;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    // Log Tag
    public final String LOG_TAG="ACTIVITY_MAIN_CONTEXT";

    // Request Codes
    public static final int SIGN_IN_REQUEST_CODE =100;

    // Firebase Auth Objects
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;


    private FragmentManager fragmentManager;

    // UI elements
    private BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.i(LOG_TAG, "onCreate()");

        /* SET UP UI ELEMENTS */
        // configure fragment manager
        fragmentManager=getSupportFragmentManager();

        // configure BNB
        bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        // implemented BottomNavigationView.OnNavigationItemSelectedListener for adding listener
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        // load HomeFragment as first fragment
        bottomNavigationView.setSelectedItemId(R.id.home);


        /* CONFIGURE FIREBASE AUTH */
        // instantiate firebase auth instance
        mAuth=FirebaseAuth.getInstance();
        // configure request for Google Sign In
        createRequest();
    }

    private void createRequest() {
        // build GoogleSignInOption using oAuth Client ID
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(getString(R.string.oAuth_client_id))    // providing oAuth Client ID
                .requestEmail() // requesting Email to be selected
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);    // Creating googleSignInClient from options specified in gso

    }



    // this method inflates activity menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // get menu inflater for activity
        MenuInflater menuInflater=getMenuInflater();
        // inflate the home_activity_menu in the menu provider
        menuInflater.inflate(R.menu.home_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signout_menu_item: // if logout button is pressed
                signOut();  // sign out
            default: return super.onOptionsItemSelected(item);
        }
    }

    public void signOut() {
        mAuth.signOut();    // signout the user from FirebaseAuth
        // but signing out from FirebaseAuth is not enough for Google Sign in
        // we also need to signout the user using Google Sign in Client
        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // switch to home fragment once sign out
                bottomNavigationView.setSelectedItemId(R.id.home);
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Class fragmentToSwitchTo=null;
        // switch to fragment depending on BNB item selected
        switch (item.getItemId()){
            case R.id.home:
                fragmentToSwitchTo=HomeFragment.class;
                break;
            case R.id.timeline:
                fragmentToSwitchTo=TimelineFragment.class;
                break;
            case R.id.add:
                // if user is logged in, show him profile
                if(mAuth.getCurrentUser()!=null) {
                    startActivity(new Intent(HomeActivity.this, AddNewEvent.class));
                    return true;
                }   // otherwise launch AddNewEvent activity
                else {
                    Toast.makeText(this, "Please sign in to access this action", Toast.LENGTH_SHORT).show();
                    launchLoginActivityWithRequestCode(SIGN_IN_REQUEST_CODE);
                    return true;
                }
            case R.id.notifications:
                fragmentToSwitchTo=NotificationFragment.class;
                break;
            case R.id.profile:
                // if user is logged in, show him profile
                if(mAuth.getCurrentUser()!=null) {
                    fragmentToSwitchTo=ProfileFragment.class;
                    break;
                }   // otherwise launch login activity
                else {
                    Toast.makeText(this, "Please sign in to access this action", Toast.LENGTH_SHORT).show();
                    launchLoginActivityWithRequestCode(SIGN_IN_REQUEST_CODE);
                    return true;
                }
        }
        return switchToFragment(fragmentToSwitchTo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SIGN_IN_REQUEST_CODE:   // got result for sign in request for profile
                // check if user signed in or not
                if(mAuth.getCurrentUser()==null) {  // user not signed in
                    // go back to home fragment
                    bottomNavigationView.setSelectedItemId(R.id.home);
                } else {
                    // show the profile fragment
                    bottomNavigationView.setSelectedItemId(R.id.profile);
                }
                break;
        }
    }

    private void launchLoginActivityWithRequestCode(int requestCode) {
        // launch LoginActivity using intent
        Intent intentToLoginActivity=new Intent(HomeActivity.this, LoginActivity.class);
        // launch login activity with request code
        startActivityForResult(intentToLoginActivity, requestCode);
    }

    boolean switchToFragment(Class<? extends  Fragment> fragmentClass) {
        if(fragmentClass!=null) {
            // replace the fragment using fragment manager
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragments_container, fragmentClass, null)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG, "onRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy()");
    }
}