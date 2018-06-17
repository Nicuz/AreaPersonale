package com.fast0n.ipersonalarea.fragments.CreditFragment;

class DataCreditFragments {

    public final String title;
    public final String description;
    public final String url;
    private final String iconText;

    DataCreditFragments(String iconText, String description, String title, String url) {
        this.title = title;
        this.description = description;
        this.iconText = iconText;
        this.url = url;
    }
}