package com.shir.androidfinalproject.Enums;

public enum LoginOption
{
    None(0),
    Facebook(1),
    Email(2),
    Gmail(3);

    private final int value;
    private LoginOption(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static LoginOption getEnum(int value){
        for (LoginOption lo: LoginOption.values()) {
            if(lo.getValue() == value)
                return lo;
        }
        return LoginOption.None;
    }
}
