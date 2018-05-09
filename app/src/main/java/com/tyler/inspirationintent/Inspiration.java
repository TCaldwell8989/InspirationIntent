package com.tyler.inspirationintent;



 // A class to hold a question and two responses


import java.util.UUID;

// Model Layer for InspirationalIntent
public class Inspiration {

    private UUID uuid;
    private String note;

    public Inspiration() {
        this(UUID.randomUUID());
    }

    public Inspiration(UUID id) {
        uuid = id;
    }

    public UUID getId() {
        return uuid;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }
}
