package com.example.futsalbook.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.futsalbook.FutsalDetailsActivity;
import com.example.futsalbook.R;
import com.example.futsalbook.adapters.BookingAdapter;
import com.example.futsalbook.models.BookingModel;
import com.example.futsalbook.models.FutsalModel;
import com.example.futsalbook.models.User;
import com.example.futsalbook.utils.ErrorUtils;
import com.example.futsalbook.utils.ItemClickSupport;
import com.example.futsalbook.utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BookingsFragment extends Fragment {
    private TextView text_view_my_bookings, text_view_not_found_bookings;
    private RecyclerView recycler_view_futsal_bookings;
    private ArrayList<BookingModel> bookingModelArrayList;
    private BookingAdapter bookingAdapter;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookings, container, false);

        text_view_my_bookings = view.findViewById(R.id.text_view_my_bookings);
        text_view_not_found_bookings = view.findViewById(R.id.text_view_not_found_bookings);
        recycler_view_futsal_bookings = view.findViewById(R.id.recycler_view_futsal_bookings);

        bookingModelArrayList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(bookingModelArrayList, getContext());

        user = SharedPrefManager.getInstance(getContext()).getUser();

        getBookings();

        return view;
    }

    private void getBookings(){
        String url = "https://rajkumargurung.com.np/futsal/get_bookings.php";

        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("not_found")) {
                    text_view_my_bookings.setVisibility(View.GONE);
                    recycler_view_futsal_bookings.setVisibility(View.GONE);
                    text_view_not_found_bookings.setVisibility(View.VISIBLE);

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
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        recycler_view_futsal_bookings.setLayoutManager(linearLayoutManager);
                        recycler_view_futsal_bookings.setAdapter(bookingAdapter);
                        //recycler_view_futsals.addItemDecoration(new SpacesItemDecoration(20));
                        bookingAdapter.notifyDataSetChanged();

                    }

                    catch (JSONException e) {
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
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
