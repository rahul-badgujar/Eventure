package com.example.myeventsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    // Log Tag
    public final String LOG_TAG="context(LoginActivity)";

    // Firebase Auth Objects
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    //Declaring a BottomNavigationView Object
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Casting the bottomNavigationView Object with R.id.bottomNavigationBar
        bottomNavigationView = findViewById(R.id.bottomNavigationBar);

        //Setting up the setOnNavigationItemSelectedListener for the bottom navigation bar
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Applying switch case for the navigation bar objects to be selected
                switch (item.getItemId()){
                    case R.id.home:
                        Toast.makeText(HomeActivity.this, "Home Screen", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.timeline:
                        Toast.makeText(HomeActivity.this, "Timeline Screen", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.add:
                        Toast.makeText(HomeActivity.this, "Add Screen", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.notifications:
                        Toast.makeText(HomeActivity.this, "Notifications Screen", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.profile:
                        Toast.makeText(HomeActivity.this, "Profile Screen", Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        });

        /* Configure Firebase Auth */
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
        // s
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
}