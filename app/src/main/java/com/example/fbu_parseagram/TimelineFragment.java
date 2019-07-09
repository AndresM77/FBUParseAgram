package com.example.fbu_parseagram;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.parse.ParseUser;

public class TimelineFragment extends Fragment {


    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private EditText descriptionInput;
    private Button createButton;
    private Button refreshButton;
    private Button logoutButton;

    public static TimelineFragment newInstance() {
        return new TimelineFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        //Setting view objects
        descriptionInput = view.findViewById(R.id.description_et);
        createButton = view.findViewById(R.id.create_btn);
        refreshButton = view.findViewById(R.id.refresh_btn);
        logoutButton = view.findViewById(R.id.logout_btn);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Log.d("HomeActivity", "Current User = " + ParseUser.getCurrentUser());
                Intent i = new Intent(getContext(), MainActivity.class);
                startActivity(i);
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (null == savedInstanceState) {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, CameraKitFragment.newInstance())
                            .commit();
                }
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}
