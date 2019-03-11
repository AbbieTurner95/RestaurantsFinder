package com.example.abbieturner.restaurantsfinder.FirebaseModels;

public class Friend {
    private String id;
    private String name;
    private String pictureUrl;
    private String email;

    public Friend(){

    }

    public String getUserId() {
        return id;
    }

    public void setUserId(String userId) {
        this.id = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureUrl() {
        if(pictureUrl == null || pictureUrl.isEmpty()){
            return "empty url";
        }else{
            return pictureUrl;
        }
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
