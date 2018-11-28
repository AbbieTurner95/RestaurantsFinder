package com.example.abbieturner.restaurantsfinder.Adapters;

import com.example.abbieturner.restaurantsfinder.Data.Restaurant;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class RestaurantJsonAdapter implements JsonDeserializer<Restaurant> {
    @Override
    public Restaurant deserialize(JsonElement json, Type typeOf, JsonDeserializationContext contex) throws JsonParseException {
        JsonElement jsonRestaurant = json.getAsJsonObject().get("restaurant");
        return new Gson().fromJson(jsonRestaurant, Restaurant.class);
    }
}

