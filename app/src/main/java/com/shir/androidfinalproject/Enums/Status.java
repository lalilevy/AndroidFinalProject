package com.shir.androidfinalproject.Enums;

public enum Status
{
    Host("Host", 10),
    Going("Going", 20),
    NotGoing("Not Going", 30),
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

    public int value() { return value; }

    public static Status fromName(String name) {

        if (name != null) {
            for (Status status : Status.values()) {
                if (name.equalsIgnoreCase(status.name)) {
                    return status;
                }
            }
        }

        throw new IllegalArgumentException("No static with display string " + name + " found");
    }

    public static Status fromValue(int value) {
        for (Status status : Status.values()) {
            if (value == status.value) {
                return status;
            }
        }

        throw new IllegalArgumentException("No Status with value " + value + " found");
    }
}