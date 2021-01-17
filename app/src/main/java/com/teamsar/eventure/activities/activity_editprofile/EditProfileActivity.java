package com.teamsar.eventure.activities.activity_editprofile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.teamsar.eventure.R;
import com.teamsar.eventure.models.User;
import com.teamsar.eventure.services.AuthenticationClient;

import java.util.Objects;

import static com.teamsar.eventure.activities.activity_home.fragments_bnb.ProfileFragment.googlePhotoUrlOfPixelSize;

public class EditProfileActivity extends AppCompatActivity {

    // UI elements
    private TextView nameTv;
    private TextView emailTv;
    private EditText bioEt;
    private ShapeableImageView profileImgIv;
    private ExtendedFloatingActionButton saveDetailsFab;

    // Authentication Client
    private AuthenticationClient authClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Edit Profile");

        // config auth client
        authClient=new AuthenticationClient(this);

        // CONFIGURE UI ELEMENTS
        // config name text view
        nameTv =findViewById(R.id.name_tv);
        // config email text view
        emailTv=findViewById(R.id.email_tv);
        // config bio edit text
        bioEt=findViewById(R.id.bio_et);
        // config profile image image view
        profileImgIv=findViewById(R.id.editprofile_profileimg_iv);
        // config save details floating action button
        saveDetailsFab=findViewById(R.id.save_details_fab);
        saveDetailsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDetails();
            }
        });

        // fill the fields with users details
        if(authClient.isUserSignedIn()) {
            FirebaseUser user=authClient.getCurrentUser();
            nameTv.setText(user.getDisplayName());
            emailTv.setText(user.getEmail());

            // load the profile image in profile picture imageview
            Uri photoUrl=user.getPhotoUrl();
            /* this photoUrl is 90px size photo Url, we want to request of higher resolution*/
            if(photoUrl!=null) {
                // get the uri in string format
                String photoUrlStr=photoUrl.toString();
                // convert the uri string to required pixel size
                photoUrlStr= googlePhotoUrlOfPixelSize(photoUrlStr,360);
                // load the picture in profile picture imageview
                Picasso.get().
                        load(photoUrlStr)
                        .placeholder(R.drawable.ic_baseline_account_circle_24)  // placeholder for network image
                        .error(R.drawable.ic_baseline_account_circle_24)    // if placeholder for error
                        .into(profileImgIv);
            }

            // fill in the bio if present
            authClient.getUserDataInstanceRef().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // deserialize the data into instance of User
                    User userData=snapshot.getValue(User.class);
                    // if user data is present
                    if(userData!=null) {
                        bioEt.setText(userData.getBio());
                    } else {
                        Toast.makeText(getApplicationContext(), "User details instance not found in database", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Something went wrong while reading users data", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    private void saveDetails() {
        // update bio
        String bio=bioEt.getText().toString();
        authClient.updateBio(bio).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}