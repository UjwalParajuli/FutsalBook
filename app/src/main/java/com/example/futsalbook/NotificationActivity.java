package com.example.futsalbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.example.futsalbook.adapters.NotificationAdapter;
import com.example.futsalbook.models.FutsalModel;
import com.example.futsalbook.models.NotificationModel;
import com.example.futsalbook.models.User;
import com.example.futsalbook.utils.ErrorUtils;
import com.example.futsalbook.utils.ItemClickSupport;
import com.example.futsalbook.utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {
    private TextView text_view_heading_notification, text_view_not_found_notification;
    private RecyclerView recycler_view_notifications;
    private ArrayList<NotificationModel> notificationModelArrayList;
    private NotificationAdapter notificationAdapter;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        text_view_heading_notification = findViewById(R.id.text_view_heading_notification);
        text_view_not_found_notification = findViewById(R.id.text_view_not_found_notification);
        recycler_view_notifications = findViewById(R.id.recycler_view_notifications);

        user = SharedPrefManager.getInstance(NotificationActivity.this).getUser();

        notificationModelArrayList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(notificationModelArrayList, NotificationActivity.this);

        getNotifications();

    }

    private void getNotifications(){
        String url = "https://rajkumargurung.com.np/futsal/get_notifications.php";

        final RequestQueue requestQueue = Volley.newRequestQueue(NotificationActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("not_found")) {
                    text_view_heading_notification.setVisibility(View.GONE);
                    recycler_view_notifications.setVisibility(View.GONE);
                    text_view_not_found_notification.setVisibility(View.VISIBLE);

                }
                else{
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonResponse;

                        for (int i = 0; i < jsonArray.length(); i++){
                            jsonResponse = jsonArray.getJSONObject(i);
                            String futsal_name = jsonResponse.getString("futsal_name");
                            String location = jsonResponse.getString("location");
                            String start_time = jsonResponse.getString("start_time");
                            String end_time = jsonResponse.getString("end_time");
                            String booked_date = jsonResponse.getString("booked_date");
                            String image = jsonResponse.getString("image");

                            NotificationModel notificationModel = new NotificationModel(start_time, end_time, booked_date, futsal_name, location, image);
                            notificationModelArrayList.add(notificationModel);
                        }

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.VERTICAL, false);
                        recycler_view_notifications.setLayoutManager(linearLayoutManager);
                        recycler_view_notifications.setAdapter(notificationAdapter);
                        //recycler_view_futsals.addItemDecoration(new SpacesItemDecoration(20));
                        notificationAdapter.notifyDataSetChanged();

                    }
                    catch (JSONException e) {
                        Toast.makeText(NotificationActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NotificationActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
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