package com.example.fbu_parseagram;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

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
        final Fragment fragment2 = new ComposeFragment();
        final Fragment fragment3 = new ProfileFragment();

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
                                fragment = fragment3;
                                break;
                        }
                        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                        return true;
                    }
                });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_favorites);

    }







}
