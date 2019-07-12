package com.example.fbu_parseagram.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("ParseComment")
public class ParseComment extends ParseObject{

    public static final String KEY_USER = "user";
    public static final String KEY_POST = "post";
    public static final String KEY_COMMENT = "parseComment";
    public static final String KEY_CREATED_AT = "createdAt";

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public String getParseComment() {
        return getString(KEY_COMMENT);
    }

    public void setParseComment(String description) {
        put(KEY_COMMENT, description);
    }

    public ParseObject getPost() {
        return getParseObject(KEY_POST);
    }

    public void setPost(ParseObject image) {
        put(KEY_POST, image);
    }

    //Querying our Post class
    public static class Query extends ParseQuery<ParseComment> {

        public Query() {
            super(ParseComment.class);
        }

        public ParseComment.Query getTop() {
            setLimit(20);
            return this;
        }

        public ParseComment.Query withUser() {
            include("user");
            return this;
        }
    }
}
