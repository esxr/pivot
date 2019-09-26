package com.example.icasapp.Auth;

import java.util.regex.Pattern;

public class FormHelper {

    public FormHelper(){}

    public boolean validateEmail(String email){

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null || email.isEmpty())
            return false;
        return pat.matcher(email).matches();
    }

    public String getFirstEmail(String email){
        email = (email.contains(";")? email.substring(0, email.indexOf(";")) : email).trim();
        return email;
    }


    public boolean validatePassword(String password){
        if(password.isEmpty() || password.length() < 6)
            return false;
        return true;
    }

    public boolean validateRegNo(String regNo){
        if(regNo.isEmpty())
            return false;
        else if(regNo.length() != 9)
            return false;
        else if(regNo.charAt(0) == '1' && (regNo.charAt(1) == '8' || regNo.charAt(1) == '9'))
            return true;
        return true;
    }

    public boolean validateField(String field){
        return !field.isEmpty();
    }

    //TODO : SENTIMENTAL ANALYSIS, MODERATION
    public boolean validateImage(){return true;}

    public void compressImage(){}
}
