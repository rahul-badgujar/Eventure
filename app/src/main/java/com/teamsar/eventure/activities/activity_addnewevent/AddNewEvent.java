package com.teamsar.eventure.activities.activity_addnewevent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.teamsar.eventure.R;
import com.theartofdev.edmodo.cropper.CropImage;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AddNewEvent extends AppCompatActivity {

    //Tag for testing purpose
    private static final String LOG_TAG = "activity-add'new'event-context";

    //global URI variable for image;
    private Uri imageURI;
    private String imageURl;

    //Set up UI elements
    private ImageView cancelIv;
    private TextView postTv;
    private ImageView imageIv;

    //UI element from the external dependency
    private SocialAutoCompleteTextView descriptionTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);

        //initiate the UI elements
        cancelIv = findViewById(R.id.cancel_iv);
        postTv = findViewById(R.id.post_tv);
        imageIv = findViewById(R.id.image_iv);
        descriptionTv = findViewById(R.id.description_tv);

        //set on click listener if user user wants to quit the activity and redirect to home-screen
        cancelIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // simply finishing this activity will cause home screen to appear
                finish();
            }
        });

        // clicking on image will trigger get local image from user
        imageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Used for cropping an image from the gallery and add it to the imageView
                external implementation which is used to auto crop and select pics
                */
                CropImage.activity().start(AddNewEvent.this);
            }
        });

        /*set on click listener to post the image and save the file to storage and in the
        RealTime Database as per the rules */

        postTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });



    }


    /*
    Method written to upload the image to the Firebase storage
    and Add all the details of the post such as:
                                                Description
                                                ImageURl
                                                postID
                                                publisher name
     */
    private void upload() {
        /*
        progress dialogue to show the progress of uploading pic.
         */
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading");
        progressDialog.show();

        if (imageURI != null) {
            StorageReference filePath = FirebaseStorage.getInstance().getReference("Posts").child(System.currentTimeMillis() + "." + getFileExtension(imageURI));
            StorageTask uploadTask = filePath.putFile(imageURI);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadUri = task.getResult();
                    assert downloadUri != null;
                    imageURl = downloadUri.toString();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                    String postId = ref.push().getKey();

                    /*
                    HashMap to store the value in the RealtimeDatabase about the information of the user
                     */
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("postId", postId);
                    map.put("imageURL", imageURl);
                    map.put("description", descriptionTv.getText().toString());
                    map.put("publisher", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                    //Testing--->Log.d(TAG, "onComplete: "+ map.toString());
                    ref.child(Objects.requireNonNull(postId)).setValue(map);


                    /*
                    Hashtags for search fragments with the help of SocialAutoCompleteTextView
                     */
                    DatabaseReference mHashTagRef = FirebaseDatabase.getInstance().getReference().child("HashTags");
                    List<String> hashTags = descriptionTv.getHashtags();
                    if (!hashTags.isEmpty()) {
                        for (String tag : hashTags) {
                            map.clear();

                            map.put("tag", tag.toLowerCase());
                            map.put("postId", postId);

                            mHashTagRef.child(tag.toLowerCase()).child(postId).setValue(map);
                        }
                    }

                    /*
                    Added sound to when something is posted.
                     */
                    MediaPlayer ring= MediaPlayer.create(AddNewEvent.this,R.raw.post_sound);
                    ring.start();

                    // task is successful
                    progressDialog.dismiss();  //Dismissing the progress dialogue
                    // go back to home activity
                    finish();
                }

                /*
                If the task is Failure the make the user know
                 */
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddNewEvent.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No image was selected", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    Function to get the extension of the image
     */
    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));
    }

    //method to check and upload image to the image view.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if everything is correctly done then set the imageURi to the URI of the selected image
        then set the image to the image selected*/
        switch (requestCode) {
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                if(resultCode==RESULT_OK) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    assert result != null;
                    imageURI = result.getUri();
                    imageIv.setImageURI(imageURI);
                }
        }

    }
}