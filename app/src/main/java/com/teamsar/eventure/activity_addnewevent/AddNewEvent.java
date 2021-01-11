package com.teamsar.eventure.activity_addnewevent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.teamsar.eventure.R;
import com.teamsar.eventure.activity_home.HomeActivity;
import com.theartofdev.edmodo.cropper.CropImage;

import java.net.URI;

public class AddNewEvent extends AppCompatActivity {

    //global URI variable for image;
    private Uri imageURI;

    //Set up UI elements
    private ImageView cancel;
    private TextView post;
    private ImageView image;

    //UI element from the external dependency
    private SocialAutoCompleteTextView description;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);

        //initiate the UI elements
        cancel = findViewById(R.id.cancel);
        post= findViewById(R.id.post);
        image = findViewById(R.id.image);
        description= findViewById(R.id.description);

        //set on click listener if user user wants to quit the activity and redirect to home-screen
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddNewEvent.this, HomeActivity.class));
                finish();
            }
        });

        //Used for cropping an image from the gallery and add it to the imageView
        CropImage.activity().start(AddNewEvent.this);

    }

    //method to check and upload image to the image view.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if everything is correctly done then set the imageURi to the URI of the selected image
        then set the image to the image selected*/
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
            CropImage.ActivityResult result= CropImage.getActivityResult(data);
            imageURI= result.getUri();
            image.setImageURI(imageURI);
        }
        //else we need to redirect it to the HomeActivity
        else{
            Toast.makeText(this, "Try Again!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddNewEvent.this, HomeActivity.class));
            finish();
        }
    }
}