package com.teamsar.eventure.activities.activity_home;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.teamsar.eventure.activities.activity_addnewevent.AddNewEvent;
import com.teamsar.eventure.activities.activity_home.fragments_bnb.HomeFragment;
import com.teamsar.eventure.activities.activity_home.fragments_bnb.NotificationFragment;
import com.teamsar.eventure.activities.activity_home.fragments_bnb.ProfileFragment;
import com.teamsar.eventure.activities.activity_home.fragments_bnb.TimelineFragment;
import com.teamsar.eventure.activities.activity_login.LoginActivity;
import com.teamsar.eventure.services.AuthenticationClient;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    // Log Tag
    public final String LOG_TAG="activity-home-context";


    // Request Codes
    public static final int SIGN_IN_REQUEST_CODE =100;

    private AuthenticationClient authClient;


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


        /* CONFIGURE AUTH CLIENT */
        authClient=new AuthenticationClient(this);
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
        authClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
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
                if(authClient.isUserSignedIn()) {
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
                if(authClient.isUserSignedIn()) {
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
                if(authClient.isUserSignedIn()) {
                    // show the profile fragment
                    bottomNavigationView.setSelectedItemId(R.id.profile);

                } else {
                    // go back to home fragment
                    bottomNavigationView.setSelectedItemId(R.id.home);
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