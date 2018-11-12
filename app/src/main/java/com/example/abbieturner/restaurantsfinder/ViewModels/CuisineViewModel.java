package com.example.abbieturner.restaurantsfinder.ViewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.example.abbieturner.restaurantsfinder.API.API;
import com.example.abbieturner.restaurantsfinder.Adapters.CuisineJsonAdapter;
import com.example.abbieturner.restaurantsfinder.Data.Cuisine;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.example.abbieturner.restaurantsfinder.Data.Cuisines;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CuisineViewModel extends ViewModel {

    private MutableLiveData<Cuisines> liveData;

    private String BASE_URL = "https://developers.zomato.com/";
    private API.ZomatoApiCalls service;

    public MutableLiveData<Cuisines> getLiveData() {
        if (liveData == null) {
            liveData = new MutableLiveData<>();
            new CuisineLoadTask().execute();

        }
        return liveData;
    }

    public class CuisineLoadTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Cuisine.class, new CuisineJsonAdapter())
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            service = retrofit.create(API.ZomatoApiCalls.class);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            service.getCuisineId("332", "53.382882", "-1.470300")
                    .enqueue(new Callback<Cuisines>() {
                        @Override
                        public void onResponse(Call<Cuisines> call, Response<Cuisines> response) {
                            liveData.setValue(response.body());
                        }

                        @Override
                        public void onFailure(Call<Cuisines> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }
}