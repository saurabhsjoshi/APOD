package com.sau.quiz2.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by saurabh on 2017-03-30.
 */

public class Picture implements Comparable<Picture>, Parcelable {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("explanation")
    @Expose
    private String explanation;
    @SerializedName("hdurl")
    @Expose
    private String hdurl;
    @SerializedName("media_type")
    @Expose
    private String mediaType;
    @SerializedName("service_version")
    @Expose
    private String serviceVersion;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("url")
    @Expose
    private String url;

    /**
     * No args constructor for use in serialization
     *
     */
    public Picture() {
    }

    /**
     *
     * @param title
     * @param hdurl
     * @param explanation
     * @param serviceVersion
     * @param date
     * @param url
     * @param mediaType
     */
    public Picture(String date, String explanation, String hdurl, String mediaType, String serviceVersion, String title, String url) {
        super();
        this.date = date;
        this.explanation = explanation;
        this.hdurl = hdurl;
        this.mediaType = mediaType;
        this.serviceVersion = serviceVersion;
        this.title = title;
        this.url = url;
    }

    public Picture(Parcel parcel) {
        this(parcel.readString(),parcel.readString(),parcel.readString(),parcel.readString(),parcel.readString(),parcel.readString(),parcel.readString());
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getHdurl() {
        //NASA API does not include https link for some reason :(
        return hdurl.replaceFirst("http:" , "https:");
    }

    public void setHdurl(String hdurl) {
        this.hdurl = hdurl;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url.replaceFirst("http:" , "https:");
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int compareTo(@NonNull Picture picture) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            return dateFormat.parse(picture.getDate()).compareTo(dateFormat.parse(getDate()));
        } catch (Exception ignore) {}

        return -1;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getDate());
        parcel.writeString(getExplanation());
        parcel.writeString(getHdurl());
        parcel.writeString(getMediaType());
        parcel.writeString(getServiceVersion());
        parcel.writeString(getTitle());
        parcel.writeString(getUrl());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Picture>() {
        @Override
        public Picture createFromParcel(Parcel parcel) {
            return new Picture(parcel);
        }

        @Override
        public Picture[] newArray(int i) {
            return new Picture[i];
        }
    };

}