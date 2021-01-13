package com.teamsar.eventure.services;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthenticationClient {

    public AuthenticationClient(Context context) {
        this.context=context;
        this.firebaseAuth= FirebaseAuth.getInstance();
        this.googleSignInClient=createGoogleSignInRequest();
    }

    // helper instances
    private Context context;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;

    // getter for firebase auth object
    public FirebaseAuth getFirebaseAuthInstance() {
        return firebaseAuth;
    }

    // getter for current user
    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    // method to check if user is signed in
    public boolean isUserSignedIn() {
        return getCurrentUser()!=null;
    }

    // creates google sign in request
    private GoogleSignInClient createGoogleSignInRequest() {
        // build GoogleSignInOption using oAuth Client ID
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(getString(R.string.oAuth_client_id))    // providing oAuth Client ID
                .requestEmail() // requesting Email to be selected
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        return GoogleSignIn.getClient(this.context, gso);    // Creating googleSignInClient from options specified in gso

    }

    // method for signing out
    public Task<Void> signOut() {
        this.firebaseAuth.signOut();    // signout the user from FirebaseAuth
        // but signing out from FirebaseAuth is not enough for Google Sign in
        // we also need to signout the user using Google Sign in Client
        return this.googleSignInClient.signOut();
    }

    // getter for google sign in intent
    public Intent getGoogleSignInIntent() {
        return googleSignInClient.getSignInIntent();
    }

    // method to get signed google account from google sign in intent
    public GoogleSignInAccount getSignedInGoogleAccountFromIntent(Intent intent) throws ApiException {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
        // Google Sign In was successful, authenticate with Firebase
        GoogleSignInAccount currentGoogleAccount = task.getResult(ApiException.class);
        return currentGoogleAccount;
    }

    // method to get current signed in google account
    public GoogleSignInAccount getCurrentSignedInGoogleAccount() {
        GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(this.context);
        return account;
    }

    // method to sign in to firebase using google account
    public Task<AuthResult> signInToFirebaseUsingGoogleAccount() {
        GoogleSignInAccount currentGoogleAccount=getCurrentSignedInGoogleAccount();
        if(currentGoogleAccount!=null) {
            // get credentials for firebase login using google account selected
            AuthCredential credential = GoogleAuthProvider.getCredential(currentGoogleAccount.getIdToken(), null);
            // sign in with firebase using credentials
            return firebaseAuth.signInWithCredential(credential);
        }
        return null;
    }
}
