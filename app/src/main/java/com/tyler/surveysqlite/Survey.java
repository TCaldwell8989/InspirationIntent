package com.tyler.surveysqlite;



 // A class to hold a question and two responses


import java.util.UUID;

public class Survey {

    private UUID uuid;
    private String question;
    private String first_response;
    private String second_response;
    private int first_score;
    private int second_score;

    public Survey() {
        this(UUID.randomUUID());
    }

    public Survey(UUID id) {
        uuid = id;
    }

    public UUID getId() {
        return uuid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getFirst_response() {
        return first_response;
    }

    public void setFirst_response(String first_response) {
        this.first_response = first_response;
    }

    public String getSecond_response() {
        return second_response;
    }

    public void setSecond_response(String second_response) {
        this.second_response = second_response;
    }

    public int getFirst_score() {
        return first_score;
    }

    public void setFirst_score(int first_score) {
        this.first_score = first_score;
    }

    public int getSecond_score() {
        return second_score;
    }

    public void setSecond_score(int second_score) {
        this.second_score = second_score;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }
}
