package com.example.fbu_parseagram;

import android.app.Application;

import com.example.fbu_parseagram.model.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("andres-parseagram")
                .clientKey("space-odyssey")
                .server("https://andresm77-parseagram.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}
