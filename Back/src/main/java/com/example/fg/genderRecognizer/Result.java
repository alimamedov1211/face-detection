package com.example.fg.genderRecognizer;

public class Result {
    private double accuracy;
    private String gender;
    private int id;

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Result{" +
                "accuracy=" + accuracy +
                ", gender='" + gender + '\'' +
                ", id=" + id +
                '}';
    }
}
