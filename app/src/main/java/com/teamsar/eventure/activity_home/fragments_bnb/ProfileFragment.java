package com.teamsar.eventure.activity_home.fragments_bnb;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.teamsar.eventure.R;


public class ProfileFragment extends Fragment {

    // UI Elements
    private ShapeableImageView profileImgIv;
    private TextView displayNameTv;
    private TextView emailTv;

    // Firebase Auth
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_profile, container, false);

        /* CONFIGURE UI*/
        // configure profile image imageview
        profileImgIv=v.findViewById(R.id.profile_img_iv);
        // configure display name textview
        displayNameTv=v.findViewById(R.id.displayname_tv);
        // configure email textview
        emailTv=v.findViewById(R.id.email_tv);

        /* CONFIGURE FIREBASE AUTH*/
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();

        // if user is logged in
        if(currentUser!=null) {
            // load the profile image in profile picture imageview
            Picasso.get().
                    load(currentUser.getPhotoUrl())
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .error(R.drawable.ic_baseline_account_circle_24)
                    .into(profileImgIv);
            // load user name in display name textview
            displayNameTv.setText(currentUser.getDisplayName());
            // load user email in email textview
            emailTv.setText(currentUser.getEmail());
        }

        return v;
    }
}