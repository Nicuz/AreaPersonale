package com.fast0n.iliad.fragments.CreditFragment;

public class DataCreditFragments {

    public String title;
    public String description;
    public String iconText;
    public String url;

    DataCreditFragments(String iconText, String description, String title, String url) {
        this.title = title;
        this.description = description;
        this.iconText = iconText;
        this.url = url;
    }
}