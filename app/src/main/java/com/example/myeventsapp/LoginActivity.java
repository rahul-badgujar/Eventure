package com.example.myeventsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    // Log tag for Logging
    static final String LOG_TAG = "context(MainActivity)";

    // constants

    // Request Codes for Intent Results
    static final int RC_SIGN_IN = 100;

    // UI Elements
    SignInButton googleSignInBtn;

    // Google Sign in Auth Objects
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* SET UP UI ELEMENTS */
        // configure signInWithGoogle Button
        googleSignInBtn = findViewById(R.id.google_sign_in_btn);
        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        /* CONFIGURE FIREBASE AUTH */
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance(); // get the instance of Firebase Auth
        // Configure Google Sign In
        createRequest();    // create Google Sign In request
    }


    // to create Sign in request
    private void createRequest() {
        // build Default GoogleSignInOption
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.oAuth_client_id))    // providing oAuth Client ID
                .requestEmail() // requesting Email to be selected
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);    // Creating googleSignInClient from options specified in gso
    }

    // to sign in user
    private void signIn() {
        // this will show up a popup to user with email id to be selected to login
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();    // intent to select google account
        startActivityForResult(signInIntent, RC_SIGN_IN);   // launch the intent
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check the request code
        if (requestCode == RC_SIGN_IN) {    // if AccountSelectionIntent has send result
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);   // get the Task for GoogleSignIn Account from intent data
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount currentGoogleAccount = task.getResult(ApiException.class);
                Toast.makeText(this, currentGoogleAccount.getEmail().toString(), Toast.LENGTH_SHORT).show();
                // if currentGoogleAccount is null, it means their is no used logged in currently
                if (currentGoogleAccount != null) {
                    // start Home Activity
                    Intent intentToHome = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intentToHome);
                }

            } catch (ApiException e) {
                String message; // error message to be shown to user
                switch (e.getStatusCode()) {    // assign error message depending on Error Code
                    case GoogleSignInStatusCodes.SIGN_IN_CANCELLED:
                        message = "Sign in cancelled by user";
                        break;
                    case GoogleSignInStatusCodes.SIGN_IN_FAILED:
                        message="Sign in failed, please retry";
                        break;
                    case GoogleSignInStatusCodes.SIGN_IN_CURRENTLY_IN_PROGRESS:
                        message="Sign in currently in progress, please wait";
                        break;
                    case GoogleSignInStatusCodes.INVALID_ACCOUNT:
                        message = "Invalid account selected";
                        break;
                    case  GoogleSignInStatusCodes.SIGN_IN_REQUIRED:
                        message="Sign in required for this account";
                        break;
                    case GoogleSignInStatusCodes.NETWORK_ERROR:
                        message="Network error occured, please retry";
                        break;
                    case GoogleSignInStatusCodes.INTERNAL_ERROR:
                        message="Internal error occured, please retry";
                        break;
                    default:
                        message="Unknown error occured with ERROR CODE "+Integer.toString(e.getStatusCode());

                }
                // Google Sign In failed, show error to user using toast
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is already signed in and update UI accordingly.
        GoogleSignInAccount currentGoogleAccount = GoogleSignIn.getLastSignedInAccount(this);   // get the google account
        // if currentGoogleAccount is null, it means their is no used logged in currently
        if (currentGoogleAccount != null) {
            // start Home Activity
            Log.i(LOG_TAG, "User already logged in: " + currentGoogleAccount.getEmail());
            Intent intentToHome = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intentToHome);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}