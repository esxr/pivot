package com.theenigma.pivot.User;

public class UserType {
    private String[] typeMap = { "student", "teacher", "alumni" };
    private String type;

    public UserType(int typeInt) {
        this.type = typeMap[typeInt - 1];
    }

    public String getType() {
        return type;
    }

    public void setType(int typeInt) {
        this.type = typeMap[typeInt - 1];
    }
}
