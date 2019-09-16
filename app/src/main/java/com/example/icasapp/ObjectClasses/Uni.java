package com.example.icasapp.ObjectClasses;

public class Uni{

    private String name, des;
    private String[] Alumni;

    public Uni()
    {

    }

    public Uni(String name, String des, String alumni[])
    {
        this.name = name;
        this.des = des;
        this.Alumni = alumni;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String[] getAlumni() {
        return Alumni;
    }

    public void setAlumni(String[] alumni) {
        Alumni = alumni;
    }
}
