package com.example.pet_care_app.Domain;

public class Price {
    private int Id;
    private String Value;

    public Price() {
    }

    @Override
    public String toString() {
        return Value;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        this.Value = value;
    }
}
