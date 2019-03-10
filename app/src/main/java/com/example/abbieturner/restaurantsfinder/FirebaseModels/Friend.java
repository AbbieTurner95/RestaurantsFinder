package com.example.abbieturner.restaurantsfinder.FirebaseModels;

public class Friend {
    private String userId;
    private String name;
    private String pictureUrl;

    public Friend(){

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
}
