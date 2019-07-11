package com.example.fbu_parseagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class ProfileActivity extends AppCompatActivity {
    private TextView userTxt;
    private ImageView profileImg;
    private Button changeBtn;
    private Button returnBtn;
    private Button setBtn;
    private Button signOutBtn;

    //Camera Variables
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    private File photoFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Setting view objects
        userTxt = findViewById(R.id.tvHandle);
        profileImg = findViewById(R.id.ivImage);
        changeBtn = findViewById(R.id.btnChange);
        setBtn = findViewById(R.id.btnSet);
        returnBtn = findViewById(R.id.btnReturn);
        signOutBtn = findViewById(R.id.btnSignOut);

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Change picture
                launchCamera();
            }
        });

        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser user = ParseUser.getCurrentUser();
                saveUser(user, photoFile);
            }
        });

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        //Setting description
        userTxt.setText(ParseUser.getCurrentUser().getUsername());
        //Setting user image
        ParseUser user = ParseUser.getCurrentUser();
        ParseFile image = (ParseFile) ParseUser.getCurrentUser().get("proImage");
        Glide.with(this).load(image.getUrl()).into(profileImg);
    }

    private void saveUser(ParseUser user, File photoFile) {
        ParseFile photo = new ParseFile(photoFile);
        user.put("proImage", photo);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    Log.d("ProfileActivity", "Successful Profile Pic");
                }
            }
        });
    }

    public void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ProfileActivity");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d("ProfileActivity", "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                profileImg.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
