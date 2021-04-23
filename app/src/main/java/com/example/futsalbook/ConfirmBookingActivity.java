package com.example.futsalbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.futsalbook.adapters.TimeAdapter;
import com.example.futsalbook.models.FutsalModel;
import com.example.futsalbook.models.TimeModel;
import com.example.futsalbook.models.User;
import com.example.futsalbook.utils.ErrorUtils;
import com.example.futsalbook.utils.SharedPrefManager;
import com.khalti.checkout.helper.Config;
import com.khalti.checkout.helper.KhaltiCheckOut;
import com.khalti.checkout.helper.OnCheckOutListener;
import com.khalti.widget.KhaltiButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ConfirmBookingActivity extends AppCompatActivity {
    private Bundle bundle;
    private FutsalModel futsalModel;
    private ArrayList<TimeModel> timeModelArrayList;
    private ArrayAdapter<TimeModel> timeModelArrayAdapter;
    private EditText edit_text_select_date;
    private Spinner spinner_available_time_list;
    private KhaltiButton button_open_khalti;
    private Calendar mCurrentDate;
    private int day, month, year;
    private ProgressBar progress_bar_confirm_booking;
    private int time_table_id;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);

        bundle = getIntent().getExtras();
        futsalModel = (FutsalModel) bundle.getSerializable("futsal_details");
        user = SharedPrefManager.getInstance(ConfirmBookingActivity.this).getUser();

        edit_text_select_date = findViewById(R.id.edit_text_select_date);
        spinner_available_time_list = findViewById(R.id.spinner_available_time_list);
        button_open_khalti = findViewById(R.id.button_open_khalti);
        progress_bar_confirm_booking = findViewById(R.id.progress_bar_confirm_booking);

        timeModelArrayList = new ArrayList<>();

        mCurrentDate = Calendar.getInstance();
        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);
        month = month + 1;
        edit_text_select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ConfirmBookingActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        edit_text_select_date.setText(dayOfMonth + "-" + month + "-" + year);

                    }
                },year, month - 1, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

            }
        });

        spinner_available_time_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TimeModel timeModel = (TimeModel) parent.getSelectedItem();
                time_table_id = timeModel.getTime_table_id();
                //Toast.makeText(ConfirmBookingActivity.this, " " + time_table_id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button_open_khalti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean error = false;
                if (edit_text_select_date.getText().toString().trim().isEmpty()){
                    error = true;
                    edit_text_select_date.setError("Please select date");
                }
                if (!error) {
                    proceed();
                }
            }
        });

        getFutsalTimes();

    }

    private void getFutsalTimes(){
        String url = "https://rajkumargurung.com.np/futsal/get_all_futsal_times.php";
        timeModelArrayList.clear();
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
                            int time_table_id = jsonResponse.getInt("time_table_id");
                            String start_time = jsonResponse.getString("start_time");
                            String end_time = jsonResponse.getString("end_time");

                            TimeModel timeModel = new TimeModel(start_time, end_time, time_table_id);
                            timeModelArrayList.add(timeModel);
                            timeModelArrayAdapter = new ArrayAdapter<>(ConfirmBookingActivity.this, android.R.layout.simple_spinner_item, timeModelArrayList);
                            timeModelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_available_time_list.setAdapter(timeModelArrayAdapter);
                        }

                    }

                    catch (JSONException e) {
                        Toast.makeText(ConfirmBookingActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ConfirmBookingActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
            }
        }) {

        };
        requestQueue.add(stringRequest);
    }

    private void proceed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmBookingActivity.this);
        builder.setTitle("Note");
        builder.setMessage("Once the payment is done, it will not be refunded. Are you sure you want to proceed?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkAvailability();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void checkAvailability(){
            String url = "https://rajkumargurung.com.np/futsal/check_availability.php";
            progress_bar_confirm_booking.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            final RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progress_bar_confirm_booking.setVisibility(View.GONE);
                    if (response.trim().equals("not_found")) {
                        openKhalti();
                    }
                    else{
                        Toast.makeText(ConfirmBookingActivity.this, "Sorry, this time is already booked. Please select another time.", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress_bar_confirm_booking.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(ConfirmBookingActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("futsal_id", String.valueOf(futsalModel.getFutsal_id()));
                    params.put("time_table_id", String.valueOf(time_table_id));
                    params.put("selected_date", edit_text_select_date.getText().toString().trim());
                    return params;
                }

            };
            requestQueue.add(stringRequest);

    }

    private void openKhalti() {
        long amount = (long)futsalModel.getPrice_per_hour();
        openKhaltiApp(amount);
    }

    private void openKhaltiApp(long amount) {
        amount *= 100;
        Config.Builder builder = new Config.Builder("test_public_key_6239eac0ae384e8a874a3514b2f294a8", String.valueOf(futsalModel.getFutsal_id()), futsalModel.getFutsal_name(),
                amount, new OnCheckOutListener() {

            @Override
            public void onSuccess(@NonNull Map<String, Object> data) {
                Log.i("success", data.toString());
                addBookings();

            }

            @Override
            public void onError(@NonNull String action, @NonNull Map<String, String> errorMap) {
                Log.e("hello", errorMap.toString());
                Toast.makeText(ConfirmBookingActivity.this, "Khalti Error", Toast.LENGTH_SHORT).show();

            }

        });

        Config config = builder.build();

        KhaltiCheckOut khaltiCheckOut = new KhaltiCheckOut(ConfirmBookingActivity.this, config);
        khaltiCheckOut.show();
    }

    private void addBookings(){
        String url = "https://rajkumargurung.com.np/futsal/add_bookings.php";
        progress_bar_confirm_booking.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        final RequestQueue requestQueue = Volley.newRequestQueue(ConfirmBookingActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress_bar_confirm_booking.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.trim().equals("success")) {
                    Toast.makeText(ConfirmBookingActivity.this, "Successfully Booked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ConfirmBookingActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (response.trim().equals("error")) {
                    Toast.makeText(ConfirmBookingActivity.this, "Error while inserting", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress_bar_confirm_booking.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(ConfirmBookingActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("futsal_id", String.valueOf(futsalModel.getFutsal_id()));
                params.put("user_id", String.valueOf(user.getUser_id()));
                params.put("time_table_id", String.valueOf(time_table_id));
                params.put("selected_date", edit_text_select_date.getText().toString().trim());
                params.put("amount", String.valueOf(futsalModel.getPrice_per_hour()));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}