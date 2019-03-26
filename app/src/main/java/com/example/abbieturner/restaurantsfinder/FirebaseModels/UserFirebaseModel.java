package com.example.abbieturner.restaurantsfinder.FirebaseModels;

import android.graphics.Bitmap;

import com.example.abbieturner.restaurantsfinder.Singletons.DateCreated;

public class UserFirebaseModel {
    private String id;
    private String name;
    private String memberSince;
    private int numberOfReviews;
    private Bitmap picture;
    private String pictureUrl;

    public UserFirebaseModel(){

    }

    public UserFirebaseModel(String id){
        this.id = id;
        this.name = "";
        this.memberSince = DateCreated.getInstance().GetDateCreated();
        this.numberOfReviews = 0;
        this.pictureUrl = "";
    }

    public UserFirebaseModel(String id, String name){
        this.id = id;
        this.name = name;
        this.memberSince = DateCreated.getInstance().GetDateCreated();
        this.numberOfReviews = 0;
        this.pictureUrl = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        if(name == null || name.isEmpty()){
            return "Name not set";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemberSince() {
        return memberSince;
    }

    public void setMemberSince(String memberSince) {
        this.memberSince = memberSince;
    }

    public int getNumberOfReviews() {
        return numberOfReviews;
    }

    public void setNumberOfReviews(int numberOfReviews) {
        this.numberOfReviews = numberOfReviews;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public String getPictureUrl() {
        if(pictureUrl == null || pictureUrl.isEmpty()){
            return "url not set";
        }
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getMemberStatus(){
        if(numberOfReviews < 5){
            return "Bronze";
        }else if(numberOfReviews < 10){
            return "Silver";
        }else{
            return "Gold";
        }
    }

    public boolean hasPicture(){
        return picture != null;
    }
}
