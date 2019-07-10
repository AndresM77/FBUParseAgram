package com.example.fbu_parseagram;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private static final String imagePath = "/sdcard/DCIM/Camer/IMG";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private EditText descriptionInput;
    private Button createButton;
    private Button refreshButton;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, TimelineFragment.newInstance())
                    .commit();
        }

        final FragmentManager fragmentManager = getSupportFragmentManager();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        // define your fragments here
        final Fragment fragment1 = new TimelineFragment();
        final Fragment fragment2 = new CameraKitFragment();

        // handle navigation selection
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment;
                        switch (item.getItemId()) {
                            case R.id.action_favorites:
                                fragment = fragment1;
                                break;
                            case R.id.action_schedules:
                                fragment = fragment2;
                                break;
                            case R.id.action_music:
                            default:
                                fragment = fragment1;
                                break;
                        }
                        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                        return true;
                    }
                });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_favorites);
        /*
        //Setting view objects
        descriptionInput = findViewById(R.id.description_et);
        createButton = findViewById(R.id.create_btn);
        refreshButton = findViewById(R.id.refresh_btn);
        logoutButton = findViewById(R.id.logout_btn);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Log.d("HomeActivity", "Current User = " + ParseUser.getCurrentUser());
                Intent i = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (null == savedInstanceState) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, Camera2BasicFragment.newInstance())
                            .commit();
                }

                final String description = descriptionInput.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();
                //take info from camera that user has
                final File file = new File(imagePath);
                final ParseFile parseFile = new ParseFile(file);

                //create a post
                createPost(description, parseFile, user);
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadTopPosts();
            }
        });
        */
    }


}
