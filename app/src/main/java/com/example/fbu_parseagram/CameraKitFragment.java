package com.example.fbu_parseagram;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.camerakit.CameraKitView;
import com.example.fbu_parseagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class CameraKitFragment extends Fragment {
    private CameraKitView cameraKitView;
    private Button captureButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_camerakit, container, false);
    }

    public static CameraKitFragment newInstance() {
        return new CameraKitFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cameraKitView = view.findViewById(R.id.camera);
        captureButton = view.findViewById(R.id.capture_btn);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireContext(), "Here 1", Toast.LENGTH_SHORT).show();
                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView cameraKitView, final byte[] capturedImage) {
                Toast.makeText(requireContext(), "Here 2", Toast.LENGTH_SHORT).show();
                        File savedPhoto = new File(Environment.getExternalStorageDirectory(), "photo.jpg");
                        try {
                Toast.makeText(requireContext(), "Here 3", Toast.LENGTH_SHORT).show();
                            //captured image is the image, now store it somewhere
                            FileOutputStream outputStream = new FileOutputStream(savedPhoto.getPath());
                            outputStream.write(capturedImage); //Captured image is there
                            outputStream.close();
                Toast.makeText(requireContext(), "Here 4", Toast.LENGTH_SHORT).show();
                        } catch (java.io.IOException e) {
                Toast.makeText(requireContext(), "Here 5", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    public void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void createPost (String description, ParseFile imageFile, ParseUser user) {
        final Post newPost = new Post();
        newPost.setDescription(description);
        newPost.setImage(imageFile);
        newPost.setUser(user);

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("HomeActivity", "Create Post Success!");
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadTopPosts() {
        // Setting up query
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        Log.d("HomeActivity", "Post[" + i + "] = "
                                + objects.get(i).getDescription()
                                + "\nusername = " + objects.get(i).getUser().getUsername()
                        );
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
