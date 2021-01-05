package com.example.myeventsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    // Log tag
    static final String LOG_TAG = "context(MainActivity)";

    // constants

    // Request Codes
    static final int RC_SIGN_IN = 100;

    // UI Elements
    SignInButton googleSignInBtn;
    Button signOutBtn;
    TextView userEmailTv;

    // Google Sign in Auth Objects
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* SET UP UI ELEMENTS */
        // configure signInWithGoogle Button
        googleSignInBtn = findViewById(R.id.google_sign_in_btn);
        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        // configure signOut Button
        signOutBtn = findViewById(R.id.sign_out_btn);
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
        // configure userEmailTextview
        userEmailTv = findViewById(R.id.user_email_tv);


        /* CONFIGURE FIREBASE AUTH */
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Configure Google Sign In
        createRequest();
    }

    // to sign out user
    private void signOut() {
        mAuth.signOut();
        updateUserEmailTv(null);
    }

    // to create Sign in request
    private void createRequest() {
        // build Default GoogleSignInOption
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.oAuth_client_id))    // providing oAuth Client ID
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    // to sign in user
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();    // intent to select google account
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(this, account.getEmail().toString(), Toast.LENGTH_SHORT).show();
                updateUserEmailTv(account); // update userEmailTextview to contain user email

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Google sign in Failed: " + e.toString(), Toast.LENGTH_SHORT).show();
                updateUserEmailTv(null);// update userEmailTextview to show default text
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        GoogleSignInAccount currentGoogleAccount = GoogleSignIn.getLastSignedInAccount(this);
        updateUserEmailTv(currentGoogleAccount);
    }

    // this method updates the userEmailTextview for current logged in Google Account
    private void updateUserEmailTv(GoogleSignInAccount currentGoogleAccount) {
        if (currentGoogleAccount == null) {
            userEmailTv.setText("Please login");
        } else {
            String userEmail = currentGoogleAccount.getEmail();
            userEmailTv.setText("Logged in as: " + userEmail);
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