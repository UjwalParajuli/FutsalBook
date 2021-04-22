package com.example.futsalbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.futsalbook.adapters.RatingAdapter;
import com.example.futsalbook.models.FutsalModel;
import com.example.futsalbook.models.RatingModel;
import com.example.futsalbook.models.TimeModel;
import com.example.futsalbook.utils.ErrorUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReviewsActivity extends AppCompatActivity {
    private Bundle bundle;
    private FutsalModel futsalModel;
    private RatingBar average_rating_bar;
    private RecyclerView recycler_view_all_ratings;
    private ArrayList<RatingModel> ratingModelArrayList;
    private RatingAdapter ratingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        bundle = getIntent().getExtras();
        futsalModel = (FutsalModel) bundle.getSerializable("futsal_details");

        average_rating_bar = findViewById(R.id.average_rating_bar);

        recycler_view_all_ratings = findViewById(R.id.recycler_view_all_ratings);
        ratingModelArrayList = new ArrayList<>();
        ratingAdapter = new RatingAdapter(ratingModelArrayList, ReviewsActivity.this);

        getAverageRating();
        getAllRatings();

    }

    private void getAverageRating(){
        String url = "https://rajkumargurung.com.np/futsal/get_average_rating.php";

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
                        double average_rating = jsonResponse.getDouble("average_rating");

                        float f = (float)average_rating;
                        average_rating_bar.setRating(f);


                    }

                    catch (JSONException e) {
                        //Toast.makeText(ReviewsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ReviewsActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
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

    private void getAllRatings(){
        String url = "https://rajkumargurung.com.np/futsal/get_all_ratings.php";

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

                        for (int i = 0; i < jsonArray.length(); i++){
                            jsonResponse = jsonArray.getJSONObject(i);
                            String full_name = jsonResponse.getString("full_name");
                            double rating = jsonResponse.getDouble("rating");

                            float f = (float)rating;
                            RatingModel ratingModel = new RatingModel(f, full_name);
                            ratingModelArrayList.add(ratingModel);
                        }

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ReviewsActivity.this, LinearLayoutManager.VERTICAL, false);
                        recycler_view_all_ratings.setLayoutManager(linearLayoutManager);
                        recycler_view_all_ratings.setAdapter(ratingAdapter);
                        ratingAdapter.notifyDataSetChanged();

                    }

                    catch (JSONException e) {
                        Toast.makeText(ReviewsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ReviewsActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
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
}