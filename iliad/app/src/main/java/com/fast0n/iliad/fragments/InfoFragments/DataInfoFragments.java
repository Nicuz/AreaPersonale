package com.fast0n.iliad.fragments.InfoFragments;

public class DataInfoFragments {

    public String textView;
    public String textView2;
    public String textView3;
    public String textView4;
    public String url;
    private int imgInfo;

    DataInfoFragments(String textView, String textView2, String textView3, String textView4, int imgInfo,
                      String url) {
        this.textView = textView;
        this.textView2 = textView2;
        this.textView3 = textView3;
        this.textView4 = textView4;
        this.imgInfo = imgInfo;
        this.url = url;
    }
}