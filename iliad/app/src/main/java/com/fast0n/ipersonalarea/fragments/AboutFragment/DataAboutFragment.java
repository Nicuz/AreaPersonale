package com.fast0n.ipersonalarea.fragments.AboutFragment;

public class DataAboutFragment {

    private String name;
    private int icon;

    DataAboutFragment(String name, int icon) {
        this.name = name;
        this.icon = icon;

    }

    public String getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }

}