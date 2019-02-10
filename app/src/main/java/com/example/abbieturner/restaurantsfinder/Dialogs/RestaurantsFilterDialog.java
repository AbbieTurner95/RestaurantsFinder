package com.example.abbieturner.restaurantsfinder.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.abbieturner.restaurantsfinder.Adapters.RestaurantsAdapter;
import com.example.abbieturner.restaurantsfinder.Data.FilterModel;
import com.example.abbieturner.restaurantsfinder.R;

public class RestaurantsFilterDialog extends DialogFragment  {
    public static String TAG;

    private RestaurantsAdapter adapter;
    SeekBar distanceBar, ratingBar;
    TextView distance, rating;
    Button search, btnClearFilter;
    EditText searchInput;
    ImageView btnClearSearchText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

        TAG = getResources().getString(R.string.RestaurantsFilterDialog);

        adapter = (RestaurantsAdapter) getArguments().getSerializable("key");
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.layout_filter_dialog, container, false);

//        Bundle b = getArguments();
//        String name = b.getString("KEY", "DEFAULT_VALUE");
//        //and get whatever you have sent

        setUpToolbar(view);

        searchInput = view.findViewById(R.id.et_search);
        distance = view.findViewById(R.id.tv_distance);
        rating = view.findViewById(R.id.tv_rating);
        distanceBar = view.findViewById(R.id.seekBarDistance);
        ratingBar = view.findViewById(R.id.seekBarRating);
        search = view.findViewById(R.id.btn_search);
        btnClearSearchText = view.findViewById(R.id.btn_clear_search_text);
        btnClearFilter = view.findViewById(R.id.btn_clear_filter);

        setValues();

        setUpListeners();

        return view;
    }

    private void setUpListeners(){
        distanceBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress == 0){
                    distance.setText("Off");
                }else{
                    distance.setText(progress + " miles");
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                FilterModel.getInstance().setDistance(seekBar.getProgress());
            }
        });

        ratingBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress == 0){
                    rating.setText("Off");
                }else{
                    rating.setText(String.valueOf(progress));
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                FilterModel.getInstance().setRating(seekBar.getProgress());
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterModel.getInstance().setSearch(searchInput.getText().toString());

                adapter.getFilter().filter("");

                closeDialog();
            }
        });

        btnClearSearchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput.setText("");
                FilterModel.getInstance().setSearch("");
            }
        });

        btnClearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearModel();
                setValues();
                adapter.getFilter().filter("");
                closeDialog();
            }
        });
    }

    private void clearModel(){
        FilterModel model = FilterModel.getInstance();
        model.setDistance(0);
        model.setRating(0);
        model.setSearch("");
    }

    private void setValues(){
        FilterModel model = FilterModel.getInstance();
        //distance.setText(model.getDistance() + " miles");
        distance.setText("Off");
        //rating.setText(String.valueOf(model.getRating()));
        rating.setText("Off");
        searchInput.setText(model.getSearch());
        distanceBar.setProgress(model.getDistance());
        ratingBar.setProgress(model.getRating());
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    private void setUpToolbar(View view){
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });
        toolbar.setTitle("Search");
    }

    private void closeDialog(){
        getActivity().getSupportFragmentManager().beginTransaction().remove(RestaurantsFilterDialog.this).commit();
    }
}
