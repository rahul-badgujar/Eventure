package com.teamsar.eventure.models;

import java.util.HashMap;
import java.util.Map;

public class User {

    public final static String ID_KEY = "id";
    public final static String NAME_KEY = "name";
    public final static String EMAIL_KEY = "email";
    public final static String IMAGE_URI_KEY = "image_uri";
    public final static String BIO_KEY = "bio";


    private String id;
    private String name;
    private String email;
    private String imageUri;
    private String bio;

    public User() {
        this.id = null;
        this.name = null;
        this.email = null;
        this.imageUri = null;
        this.bio = null;
    }

    public User(String id, String name, String email, String imageUri, String bio) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.imageUri = imageUri;
        this.bio = bio;
    }

    public User(Map<String, Object> m) {
        this(); // allots all the field null value
        if (m.get(ID_KEY) != null)
            this.id = m.get(ID_KEY).toString();
        if (m.get(NAME_KEY) != null)
            this.name = m.get(NAME_KEY).toString();
        if (m.get(EMAIL_KEY) != null)
            this.email = m.get(EMAIL_KEY).toString();
        if (m.get(IMAGE_URI_KEY) != null)
            this.imageUri = m.get(IMAGE_URI_KEY).toString();
        if (m.get(BIO_KEY) != null)
            this.bio = m.get(BIO_KEY).toString();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> m=new HashMap<>();
        m.put(ID_KEY, this.id);
        m.put(NAME_KEY, this.name);
        m.put(EMAIL_KEY, this.email);
        m.put(IMAGE_URI_KEY, this.imageUri);
        m.put(BIO_KEY, this.bio);
        return m;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUri() {
        return this.imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getBio() {
        return this.bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

}
