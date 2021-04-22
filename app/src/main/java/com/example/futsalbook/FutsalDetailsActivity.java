package com.example.futsalbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.futsalbook.adapters.TimeAdapter;
import com.example.futsalbook.models.FutsalModel;
import com.example.futsalbook.models.TimeModel;
import com.example.futsalbook.models.User;
import com.example.futsalbook.utils.ErrorUtils;
import com.example.futsalbook.utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FutsalDetailsActivity extends AppCompatActivity {
    private Bundle bundle;
    private FutsalModel futsalModel;
    private ArrayList<TimeModel> timeModelArrayList;
    private TimeAdapter timeAdapter;
    private Toolbar toolbar;
    private ImageView futsal_image_full, image_view_saved, image_view_not_saved;
    private TextView text_view_futsal_name, text_view_futsal_location, text_view_futsal_phone, text_view_futsal_price, text_view_futsal_description, text_view_sorry_message;
    private Button button_book_now, button_see_all_reviews;
    private RecyclerView recycler_view_available_time;
    private RatingBar ratingBar;
    private User user;
    private float insert_rating, old_rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_futsal_details);

        toolbar = (Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bundle = getIntent().getExtras();
        futsalModel = (FutsalModel) bundle.getSerializable("futsal_details");

        timeModelArrayList = new ArrayList<>();
        timeAdapter = new TimeAdapter(timeModelArrayList, FutsalDetailsActivity.this);
        timeModelArrayList.clear();

        user = SharedPrefManager.getInstance(FutsalDetailsActivity.this).getUser();

        futsal_image_full = findViewById(R.id.futsal_image_full);
        image_view_saved = findViewById(R.id.image_view_saved);
        image_view_not_saved = findViewById(R.id.image_view_not_saved);
        text_view_futsal_name = findViewById(R.id.text_view_futsal_name);
        text_view_futsal_location = findViewById(R.id.text_view_futsal_location);
        text_view_futsal_phone = findViewById(R.id.text_view_futsal_phone);
        text_view_futsal_price = findViewById(R.id.text_view_futsal_price);
        text_view_futsal_description = findViewById(R.id.text_view_futsal_description);
        text_view_sorry_message = findViewById(R.id.text_view_sorry_message);
        button_book_now = findViewById(R.id.button_book_now);
        button_see_all_reviews = findViewById(R.id.button_see_all_reviews);
        recycler_view_available_time = findViewById(R.id.recycler_view_available_time);
        ratingBar = findViewById(R.id.ratingBar);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                insert_rating = rating;
                if (insert_rating != old_rating){
                    insertRating();
                }
            }
        });

        button_see_all_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("futsal_details", futsalModel);
                Intent intent = new Intent(FutsalDetailsActivity.this, ReviewsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        image_view_saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeBookmark();
            }
        });

        image_view_not_saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertBookmark();
            }
        });

        button_book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookFutsal();
            }
        });

        setData();
        checkBookmark();
        getAvailableTimes();
        getRating();

    }

    private void setData(){
        this.setTitle(futsalModel.getFutsal_name());
        Glide.with(this).load(futsalModel.getImage()).into(futsal_image_full);
        text_view_futsal_name.setText(futsalModel.getFutsal_name());
        text_view_futsal_location.setText(futsalModel.getLocation());
        text_view_futsal_phone.setText(futsalModel.getPhone());
        text_view_futsal_price.setText("Rs. " + futsalModel.getPrice_per_hour() + "/hr");
        text_view_futsal_description.setText(futsalModel.getDescription());
    }

    private void checkBookmark(){
        String url = "https://rajkumargurung.com.np/futsal/check_bookmark.php";
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("not_found")) {
                    image_view_saved.setVisibility(View.GONE);
                    image_view_not_saved.setVisibility(View.VISIBLE);
                }
                else{
                    image_view_not_saved.setVisibility(View.GONE);
                    image_view_saved.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FutsalDetailsActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("futsal_id", String.valueOf(futsalModel.getFutsal_id()));
                params.put("user_id", String.valueOf(user.getUser_id()));
                return params;
            }

        };
        requestQueue.add(stringRequest);

    }

    private void getAvailableTimes(){
        String url = "https://rajkumargurung.com.np/futsal/get_available_times.php";
        timeModelArrayList.clear();
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("not_found")) {
                    recycler_view_available_time.setVisibility(View.INVISIBLE);
                    text_view_sorry_message.setVisibility(View.VISIBLE);
                }
                else{
                    timeModelArrayList.clear();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonResponse;

                        for (int i = 0; i < jsonArray.length(); i++){
                            jsonResponse = jsonArray.getJSONObject(i);
                            int time_table_id = jsonResponse.getInt("time_table_id");
                            String start_time = jsonResponse.getString("start_time");
                            String end_time = jsonResponse.getString("end_time");

                            TimeModel timeModel = new TimeModel(start_time, end_time, time_table_id);
                            timeModelArrayList.add(timeModel);
                        }

                        if (timeModelArrayList.size() <= 0){
                            recycler_view_available_time.setVisibility(View.INVISIBLE);
                            text_view_sorry_message.setVisibility(View.VISIBLE);
                        }
                        else{
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FutsalDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            recycler_view_available_time.setLayoutManager(linearLayoutManager);
                            recycler_view_available_time.setAdapter(timeAdapter);
                            timeAdapter.notifyDataSetChanged();
                        }


                    }

                    catch (JSONException e) {
                        Toast.makeText(FutsalDetailsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FutsalDetailsActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("futsal_id", String.valueOf(futsalModel.getFutsal_id()));
                return params;
            }

        };
        requestQueue.add(stringRequest);

    }

    private void getRating(){
        String url = "https://rajkumargurung.com.np/futsal/get_rating.php";
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("not_found")) {

                }
                else{
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonResponse;

                        jsonResponse = jsonArray.getJSONObject(0);
                        double rating = jsonResponse.getDouble("rating");
                        float f = (float) rating;
                        old_rating = f;
                        ratingBar.setRating(f);
                    }
                    catch (JSONException e) {
                        Toast.makeText(FutsalDetailsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FutsalDetailsActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("futsal_id", String.valueOf(futsalModel.getFutsal_id()));
                params.put("user_id", String.valueOf(user.getUser_id()));
                return params;
            }

        };
        requestQueue.add(stringRequest);

    }

    private void insertRating(){
        String url = "https://rajkumargurung.com.np/futsal/insert_rating.php";
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        final RequestQueue requestQueue = Volley.newRequestQueue(FutsalDetailsActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.trim().equals("success")) {
                    Toast.makeText(FutsalDetailsActivity.this, "Thank you for your rating", Toast.LENGTH_SHORT).show();
                }
                else if (response.trim().equals("delete_error")) {
                    Toast.makeText(getApplicationContext(), "Error while deleting", Toast.LENGTH_SHORT).show();
                }
                else if (response.trim().equals("insert_errror")) {
                    Toast.makeText(getApplicationContext(), "Error while inserting", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(FutsalDetailsActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("futsal_id", String.valueOf(futsalModel.getFutsal_id()));
                params.put("user_id", String.valueOf(user.getUser_id()));
                params.put("rating", String.valueOf(insert_rating));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void removeBookmark(){
        String url = "https://rajkumargurung.com.np/futsal/remove_bookmark.php";
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                    image_view_saved.setVisibility(View.GONE);
                    image_view_not_saved.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(FutsalDetailsActivity.this, "Error while removing", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FutsalDetailsActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("futsal_id", String.valueOf(futsalModel.getFutsal_id()));
                params.put("user_id", String.valueOf(user.getUser_id()));
                return params;
            }

        };
        requestQueue.add(stringRequest);
    }

    private void insertBookmark(){
        String url = "https://rajkumargurung.com.np/futsal/insert_bookmark.php";
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                    image_view_not_saved.setVisibility(View.GONE);
                    image_view_saved.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(FutsalDetailsActivity.this, "Error while saving", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FutsalDetailsActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("futsal_id", String.valueOf(futsalModel.getFutsal_id()));
                params.put("user_id", String.valueOf(user.getUser_id()));
                return params;
            }

        };
        requestQueue.add(stringRequest);

    }

    private void bookFutsal(){
        if (text_view_sorry_message.getVisibility() == View.VISIBLE){
            Toast.makeText(FutsalDetailsActivity.this, "Sorry, there is no booking left for today.", Toast.LENGTH_SHORT).show();
        }
        else{
            Bundle bundle = new Bundle();
            bundle.putSerializable("futsal_details", futsalModel);
            Intent intent = new Intent(FutsalDetailsActivity.this, ConfirmBookingActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }
}