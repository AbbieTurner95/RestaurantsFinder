package com.example.abbieturner.restaurantsfinder.Adapters;

import com.example.abbieturner.restaurantsfinder.Data.Review;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class ReviewJsonAdapter implements JsonDeserializer<Review> {
    @Override
    public Review deserialize(JsonElement json, Type typeOf, JsonDeserializationContext context) throws JsonParseException {
        JsonElement jsonReview = json.getAsJsonObject().get("review");
        return new Gson().fromJson(jsonReview, Review.class);
    }


}
