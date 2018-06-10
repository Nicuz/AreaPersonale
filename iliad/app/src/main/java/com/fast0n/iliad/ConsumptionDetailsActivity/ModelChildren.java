package com.fast0n.iliad.ConsumptionDetailsActivity;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelChildren implements Parcelable{
    private String a;
    private String b;
    private String c;
    private String d;
    private String e;
    private String f;


    public ModelChildren(Parcel in) {

        a = in.readString();
        b = in.readString();
        c = in.readString();
        d = in.readString();
        e = in.readString();
        e = in.readString();
        f = in.readString();
    }

    public String getA() {
        return a;
    }
    public String getB() {
        return b;
    }
    public String getC() {
        return c;
    }
    public String getD() {
        return d;
    }
    public String getE() {
        return e;
    }
    public String getF() {
        return f;
    }

    public void setA(String a) {
        this.a = a;
    }
    public void setB(String b) {
        this.b = b;
    }
    public void setC(String c) {
        this.c = c;
    }
    public void setD(String d) {
        this.d = d;
    }
    public void setE(String e) {
        this.e = e;
    }
    public void setF(String f) {
        this.f = f;
    }


    public ModelChildren(String a, String b,String c,String d,String e,String f) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(a);
        dest.writeString(b);
        dest.writeString(c);
        dest.writeString(d);
        dest.writeString(e);
        dest.writeString(f);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ModelChildren> CREATOR = new Creator<ModelChildren>() {
        @Override
        public ModelChildren createFromParcel(Parcel in) {
            return new ModelChildren(in);
        }

        @Override
        public ModelChildren[] newArray(int size) {
            return new ModelChildren[size];
        }
    };
}
