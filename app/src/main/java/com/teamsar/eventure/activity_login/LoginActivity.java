package com.teamsar.eventure.activity_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
import com.teamsar.eventure.services.AuthenticationClient;

public class LoginActivity extends AppCompatActivity {


    // Log tag for Logging
    public final String LOG_TAG="activity-login-context";

    // constants

    // Request Codes for Intent Results
    static final int RC_SIGN_IN = 100;



    // UI Elements
    private SignInButton googleSignInBtn;
    private TextView skipTv;

    private AuthenticationClient authClient;




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



        /* CONFIGURE AUTHENTICATION CLIENT */
        authClient=new AuthenticationClient(this);
    }

    // to sign in user
    private void signIn() {
        // this will show up a popup to user with email id to be selected to login
        Intent signInIntent = authClient.getGoogleSignInIntent();    // intent to select google account
        startActivityForResult(signInIntent, RC_SIGN_IN);   // launch the intent
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check the request code
        if (requestCode == RC_SIGN_IN) {    // if AccountSelectionIntent has send result
            try {

                // if currentGoogleAccount is null, it means their is no used logged in currently
                GoogleSignInAccount currentGoogleAccount=authClient.getSignedInGoogleAccountFromIntent(data);
                if (currentGoogleAccount != null) {
                    Log.i(LOG_TAG, "Google Account selected: " + currentGoogleAccount.getEmail());
                    // google account is selected successfully, now we sign in using firebase
                    firebaseSignInUsingGoogleAccount();
                } else {
                    Toast.makeText(getApplicationContext(), "Couldn't select Google Account", Toast.LENGTH_SHORT).show();
                }

            } catch (ApiException e) {
                // TODO: define user defined exceptions for this codes
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

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Unknown Exception: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseSignInUsingGoogleAccount() {
        Task<AuthResult> authResultTask=authClient.signInToFirebaseUsingGoogleAccount();
        // sign in with firebase using credentials
        authResultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i(LOG_TAG, "Firebase sign in successful: " + authClient.getCurrentUser().getEmail());
                    Toast.makeText(getApplicationContext(), authClient.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
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