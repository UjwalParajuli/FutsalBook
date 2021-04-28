package com.example.futsalbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.futsalbook.adapters.BookingAdapter;
import com.example.futsalbook.models.BookingModel;
import com.example.futsalbook.models.User;
import com.example.futsalbook.utils.ErrorUtils;
import com.example.futsalbook.utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BookingHistory extends AppCompatActivity {
    private User user;
    private TextView text_view_my_bookings_history, text_view_not_found_bookings_history;
    private RecyclerView recycler_view_futsal_bookings_history;
    private ArrayList<BookingModel> bookingModelArrayList;
    private BookingAdapter bookingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);

        this.setTitle("Booking History");
        
        user = SharedPrefManager.getInstance(this).getUser();
        text_view_my_bookings_history = findViewById(R.id.text_view_my_bookings_history);
        text_view_not_found_bookings_history = findViewById(R.id.text_view_not_found_bookings_history);
        recycler_view_futsal_bookings_history = findViewById(R.id.recycler_view_futsal_bookings_history);
        
        bookingModelArrayList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(bookingModelArrayList, BookingHistory.this);
        
        getBookingHistory();
    }
    
    private void getBookingHistory(){
        String url = "https://rajkumargurung.com.np/futsal/get_bookings.php";

        final RequestQueue requestQueue = Volley.newRequestQueue(BookingHistory.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("not_found")) {
                    text_view_my_bookings_history.setVisibility(View.GONE);
                    recycler_view_futsal_bookings_history.setVisibility(View.GONE);
                    text_view_not_found_bookings_history.setVisibility(View.VISIBLE);

                }
                else{
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonResponse;

                        for (int i = 0; i < jsonArray.length(); i++){
                            jsonResponse = jsonArray.getJSONObject(i);
                            String futsal_name = jsonResponse.getString("futsal_name");
                            String location = jsonResponse.getString("location");
                            String user_name = jsonResponse.getString("full_name");
                            String start_time = jsonResponse.getString("start_time");
                            String end_time = jsonResponse.getString("end_time");
                            String booked_date = jsonResponse.getString("booked_date");
                            String booked_on = jsonResponse.getString("booked_on");

                            BookingModel bookingModel = new BookingModel(futsal_name, location, user_name, start_time, end_time, booked_date, booked_on);
                            bookingModelArrayList.add(bookingModel);
                        }
                        Collections.reverse(bookingModelArrayList);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BookingHistory.this, LinearLayoutManager.VERTICAL, false);
                        recycler_view_futsal_bookings_history.setLayoutManager(linearLayoutManager);
                        recycler_view_futsal_bookings_history.setAdapter(bookingAdapter);
                        //recycler_view_futsals.addItemDecoration(new SpacesItemDecoration(20));
                        bookingAdapter.notifyDataSetChanged();

                    }

                    catch (JSONException e) {
                        Toast.makeText(BookingHistory.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BookingHistory.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(user.getUser_id()));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}