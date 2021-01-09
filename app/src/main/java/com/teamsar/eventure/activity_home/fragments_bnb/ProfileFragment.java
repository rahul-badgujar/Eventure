package com.teamsar.eventure.activity_home.fragments_bnb;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.teamsar.eventure.R;


public class ProfileFragment extends Fragment {

    // UI Elements
    private ShapeableImageView profileImgIv;

    // Firebase Auth
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_profile, container, false);

        /* CONFIGURE FIREBASE AUTH*/
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();

        /* CONFIGURE UI*/
        // configure profile image imageview
        profileImgIv=v.findViewById(R.id.profile_img_iv);
        // if user is logged in
        if(currentUser!=null) {
            // load the profile image in imageview
            Picasso.get().
                    load(currentUser.getPhotoUrl())
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .error(R.drawable.ic_baseline_account_circle_24)
                    .into(profileImgIv);
        }

        return v;
    }
}