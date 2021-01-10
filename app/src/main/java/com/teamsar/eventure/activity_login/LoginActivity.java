package com.teamsar.eventure.activity_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.teamsar.eventure.R;
import com.teamsar.eventure.activity_home.HomeActivity;

public class LoginActivity extends AppCompatActivity {


    // Log tag for Logging
    static final String LOG_TAG = "context(LoginActivity)";

    // constants

    // Request Codes for Intent Results
    static final int RC_SIGN_IN = 100;

    // UI Elements
    private SignInButton googleSignInBtn;
    private TextView skipTv;

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
        // configure skip Textview
        skipTv = findViewById(R.id.skip_tv);
        skipTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Logged in as guest", Toast.LENGTH_SHORT).show();
                // sign in skipped, finish the activity
                finish();
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
                // if currentGoogleAccount is null, it means their is no used logged in currently
                if (currentGoogleAccount != null) {
                    Log.i(LOG_TAG, "Google Account selected: " + currentGoogleAccount.getEmail());
                    // google account is selected successfully, now we sign in using firebase
                    firebaseSignInUsingGoogleAccount(currentGoogleAccount);
                } else {
                    Toast.makeText(getApplicationContext(), "Couldn't select Google Account", Toast.LENGTH_SHORT).show();
                }

            } catch (ApiException e) {
                String message; // error message to be shown to user
                switch (e.getStatusCode()) {    // assign error message depending on Error Code
                    case GoogleSignInStatusCodes.SIGN_IN_CANCELLED:
                        message = "Sign in cancelled by user";
                        break;
                    case GoogleSignInStatusCodes.SIGN_IN_FAILED:
                        message = "Sign in failed, please retry";
                        break;
                    case GoogleSignInStatusCodes.SIGN_IN_CURRENTLY_IN_PROGRESS:
                        message = "Sign in currently in progress, please wait";
                        break;
                    case GoogleSignInStatusCodes.INVALID_ACCOUNT:
                        message = "Invalid account selected";
                        break;
                    case GoogleSignInStatusCodes.SIGN_IN_REQUIRED:
                        message = "Sign in required for this account";
                        break;
                    case GoogleSignInStatusCodes.NETWORK_ERROR:
                        message = "Network error occurred, please retry";
                        break;
                    case GoogleSignInStatusCodes.INTERNAL_ERROR:
                        message = "Internal error occurred, please retry";
                        break;
                    default:
                        message = "Unknown error occurred with ERROR CODE " + e.getStatusCode();

                }
                // Google Sign In failed, show error to user using toast
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void firebaseSignInUsingGoogleAccount(GoogleSignInAccount currentGoogleAccount) {
        // get credentials for firebase login using google account selected
        AuthCredential credential = GoogleAuthProvider.getCredential(currentGoogleAccount.getIdToken(), null);
        // sign in with firebase using credentials
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i(LOG_TAG, "Firebase sign in successful: " + currentGoogleAccount.getEmail());
                    Toast.makeText(getApplicationContext(), mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                    // sign in successful, finish the activity
                    finish();
                } else {
                    Log.i(LOG_TAG, "Firebase sign in failed ");
                    Toast.makeText(getApplicationContext(), "Sign in failed, retry", Toast.LENGTH_SHORT).show();
                }
            }
        });
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