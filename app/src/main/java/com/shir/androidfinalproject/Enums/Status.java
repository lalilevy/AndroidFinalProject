package com.shir.androidfinalproject.Enums;

public enum Status
{
    None("None", -1),
    Host("Host", 10),
    Going("Going", 20),
    Ignore("Ignore", 30),
    Invited("Invited", 40);

    private String name;
    private int value;

    private Status(String strFriendlyName, int nValue){
        this.name = strFriendlyName;
        this.value = nValue;
    }

    @Override public String toString(){
        return name;
    }

    public int getValue() { return value; }
    public String getName() { return name; }

    public static Status fromName(String name) {

        if (name != null) {
            for (Status status : Status.values()) {
                if (name.equalsIgnoreCase(status.name)) {
                    return status;
                }
            }
        }

        return Status.None;
    }

    public static Status fromValue(int value) {
        for (Status status : Status.values()) {
            if (value == status.value) {
                return status;
            }
        }

        return Status.None;
    }
}