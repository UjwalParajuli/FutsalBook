package com.example.futsalbook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.futsalbook.utils.ErrorUtils;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private EditText edit_text_full_name, edit_text_sign_up_email, edit_text_sign_up_phone;
    private TextInputLayout password_toggle;
    private Button button_sign_up;
    private TextView text_view_log_in;
    private ProgressBar progress_bar_create_account;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 6 characters
                    "$");

    private static final Pattern PHONE_PATTERN = Pattern.compile("^[+]?[0-9]{10,13}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edit_text_full_name = findViewById(R.id.edit_text_full_name);
        edit_text_sign_up_email = findViewById(R.id.edit_text_sign_up_email);
        edit_text_sign_up_phone = findViewById(R.id.edit_text_sign_up_phone);
        password_toggle = (TextInputLayout)findViewById(R.id.password_toggle);
        button_sign_up = findViewById(R.id.button_sign_up);
        text_view_log_in = findViewById(R.id.text_view_log_in);
        progress_bar_create_account = findViewById(R.id.progress_bar_create_account);

        edit_text_full_name.addTextChangedListener(nameTextWatcher);
        edit_text_sign_up_email.addTextChangedListener(emailTextWatcher);
        edit_text_sign_up_phone.addTextChangedListener(phoneTextWatcher);
        password_toggle.getEditText().addTextChangedListener(passwordTextWatcher);

        button_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

        text_view_log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });

    }

    private TextWatcher nameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String full_name = edit_text_full_name.getText().toString().trim();
            if (!full_name.matches("[a-zA-Z\\s]+")){
                edit_text_full_name.setError("Please enter your valid name");
                button_sign_up.setEnabled(false);
            }
            else {
                edit_text_full_name.setError(null);
                button_sign_up.setEnabled(true);
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
            String email = edit_text_sign_up_email.getText().toString().trim();
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edit_text_sign_up_email.setError("Please enter a valid email address");
                button_sign_up.setEnabled(false);
            }
            else {
                edit_text_sign_up_email.setError(null);
                button_sign_up.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            password_toggle.setHintEnabled(false);

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String pass = password_toggle.getEditText().getText().toString().trim();
            if (!PASSWORD_PATTERN.matcher(pass).matches()){
                password_toggle.setHintEnabled(false);
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) password_toggle.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                password_toggle.setLayoutParams(params);
                password_toggle.setError("Password must contain one uppercase, lowercase, digit, special character and must be 6 character long");
                button_sign_up.setEnabled(false);
            }
            else {
                password_toggle.setHintEnabled(false);
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) password_toggle.getLayoutParams();
                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics());
                params.height = height;
                password_toggle.setLayoutParams(params);
                password_toggle.setError(null);
                button_sign_up.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
            password_toggle.setHintEnabled(false);

        }
    };

    private TextWatcher phoneTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String phone = edit_text_sign_up_phone.getText().toString().trim();
            if (!PHONE_PATTERN.matcher(phone).matches()){
                edit_text_sign_up_phone.setError("Please enter valid phone number");
                button_sign_up.setEnabled(false);
            }
            else {
                edit_text_sign_up_phone.setError(null);
                button_sign_up.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void openLoginActivity() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void createAccount(){
        final String full_name, email, password, phone;
        boolean error = false;
        String url = "https://rajkumargurung.com.np/futsal/create_user.php";
        full_name = edit_text_full_name.getText().toString().trim();
        email = edit_text_sign_up_email.getText().toString().trim();
        password = password_toggle.getEditText().getText().toString().trim();
        phone = edit_text_sign_up_phone.getText().toString().trim();

        if (full_name.isEmpty()){
            edit_text_full_name.setError("Please fill this field");
            error = true;
        }
        if (!full_name.matches("[a-zA-Z\\s]+")){
            edit_text_full_name.setError("Please enter your valid name");
            error = true;
        }
        if (email.isEmpty()){
            edit_text_sign_up_email.setError("Please fill this field");
            error = true;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edit_text_sign_up_email.setError("Please enter a valid email address");
            error = true;
        }
        if (password.isEmpty()){
            password_toggle.setError("Please fill this field");
            error = true;
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            password_toggle.setError("Password too weak. Must be 6 character long");
            error = true;
        }
        if (phone.isEmpty()){
            edit_text_sign_up_phone.setError("Please fill this field");
            error = true;
        }
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            edit_text_sign_up_phone.setError("Please enter valid phone number");
            error = true;
        }

        if (!error) {
            progress_bar_create_account.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            final RequestQueue requestQueue = Volley.newRequestQueue(SignUpActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progress_bar_create_account.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    if (response.trim().equals("success")) {
                        Toast.makeText(SignUpActivity.this, "Account successfully created", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                    else if (response.trim().equals("dbError")) {
                        Toast.makeText(getApplicationContext(), "Error while inserting", Toast.LENGTH_SHORT).show();
                    }
                    else if (response.trim().equals("dbError2")) {
                        Toast.makeText(getApplicationContext(), "Error while fetching", Toast.LENGTH_SHORT).show();
                    }
                    else if (response.trim().equals("email_taken")) {
                        Toast.makeText(getApplicationContext(), "Email already registered", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress_bar_create_account.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(SignUpActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("full_name", full_name);
                    params.put("email", email);
                    params.put("password", password);
                    params.put("phone", phone);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setMessage("Are you sure want to exit the app?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
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


}