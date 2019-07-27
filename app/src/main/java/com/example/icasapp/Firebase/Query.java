package com.example.icasapp.Firebase;

public class Query {
    private String property;
    private String value;

    public Query(String property, String value) {
        this.property = property;
        this.value = value;
    }

    public Query(String[] queryParams) { try {
        this.property = queryParams[0];
        this.value = queryParams[1];
    } catch(Exception e) {} }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
