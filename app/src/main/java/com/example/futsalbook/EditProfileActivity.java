package com.example.futsalbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.futsalbook.fragments.ProfileFragment;
import com.example.futsalbook.models.User;
import com.example.futsalbook.utils.ErrorUtils;
import com.example.futsalbook.utils.SharedPrefManager;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class EditProfileActivity extends AppCompatActivity {
    private EditText edit_text_edit_full_name, edit_text_edit_email, edit_text_edit_phone;
    private Button button_edit_profile;
    private User user;
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[+]?[0-9]{10,13}$");
    private int old_user_id;
    private String old_email, old_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        user = SharedPrefManager.getInstance(this).getUser();

        old_user_id = user.getUser_id();
        old_email = user.getEmail();
        old_password = user.getPassword();

        edit_text_edit_full_name = findViewById(R.id.edit_text_edit_full_name);
        edit_text_edit_email = findViewById(R.id.edit_text_edit_email);
        edit_text_edit_phone = findViewById(R.id.edit_text_edit_phone);
        button_edit_profile = findViewById(R.id.button_edit_profile);

        edit_text_edit_full_name.setText(user.getFull_name());
        edit_text_edit_email.setText(user.getEmail());
        edit_text_edit_phone.setText(user.getPhone());

        button_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        edit_text_edit_full_name.addTextChangedListener(nameTextWatcher);
        edit_text_edit_email.addTextChangedListener(emailTextWatcher);
        edit_text_edit_phone.addTextChangedListener(phoneTextWatcher);

    }

    private TextWatcher nameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String full_name = edit_text_edit_full_name.getText().toString().trim();
            if (!full_name.matches("[a-zA-Z\\s]+")){
                edit_text_edit_full_name.setError("Please enter your name properly");
                button_edit_profile.setEnabled(false);
            }
            else {
                edit_text_edit_full_name.setError(null);
                button_edit_profile.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher emailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String email = edit_text_edit_email.getText().toString().trim();
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edit_text_edit_email.setError("Please enter a valid email address");
                button_edit_profile.setEnabled(false);
            }
            else {
                edit_text_edit_email.setError(null);
                button_edit_profile.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher phoneTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String phone = edit_text_edit_phone.getText().toString().trim();
            if (!PHONE_PATTERN.matcher(phone).matches()){
                edit_text_edit_phone.setError("Please enter valid phone number");
                button_edit_profile.setEnabled(false);
            }
            else {
                edit_text_edit_phone.setError(null);
                button_edit_profile.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void updateProfile(){
        final String full_name, new_email, phone;
        boolean error = false;
        String url = "https://rajkumargurung.com.np/futsal/update_profile.php";
        full_name = edit_text_edit_full_name.getText().toString().trim();
        new_email = edit_text_edit_email.getText().toString().trim();
        phone = edit_text_edit_phone.getText().toString().trim();

        if (full_name.isEmpty()){
            edit_text_edit_full_name.setError("Please fill this field");
            error = true;
        }
        if (!full_name.matches("[a-zA-Z\\s]+")){
            edit_text_edit_full_name.setError("Please enter your name properly");
            error = true;
        }
        if (new_email.isEmpty()){
            edit_text_edit_email.setError("Please fill this field");
            error = true;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(new_email).matches()) {
            edit_text_edit_email.setError("Please enter a valid email address");
            error = true;
        }
        if (phone.isEmpty()){
            edit_text_edit_phone.setError("Please fill this field");
            error = true;
        }
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            edit_text_edit_phone.setError("Please enter valid phone number");
            error = true;
        }

        if (!error) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            final RequestQueue requestQueue = Volley.newRequestQueue(EditProfileActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    if (response.trim().equals("success")) {
                        Toast.makeText(EditProfileActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();

                        User user = new User(old_user_id, full_name, new_email, phone, old_password);
                        SharedPrefManager.getInstance(EditProfileActivity.this).userLogin(user);
                        Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                    else if (response.trim().equals("dbError")) {
                        Toast.makeText(EditProfileActivity.this, "Error while inserting", Toast.LENGTH_SHORT).show();
                    }
                    else if (response.trim().equals("dbError2")) {
                        Toast.makeText(EditProfileActivity.this, "Error while fetching", Toast.LENGTH_SHORT).show();
                    }
                    else if (response.trim().equals("email_taken")) {
                        Toast.makeText(EditProfileActivity.this, "Email already registered", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(EditProfileActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("full_name", full_name);
                    params.put("new_email", new_email);
                    params.put("old_email", old_email);
                    params.put("user_id", String.valueOf(user.getUser_id()));
                    params.put("phone", phone);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }

    }
}