package com.fast0n.iliad.fragments.CreditFragment;

public class DataCreditFragments {

    public String title;
    public String description;
    public int imgInfo;
    public String iconText;

    public DataCreditFragments(String iconText, String description, String title, int imgInfo) {
        this.title = title;
        this.description = description;
        this.imgInfo = imgInfo;
        this.iconText = iconText;
    }
}