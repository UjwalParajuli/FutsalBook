package com.example.futsalbook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.futsalbook.models.User;
import com.example.futsalbook.utils.ErrorUtils;
import com.example.futsalbook.utils.SharedPrefManager;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText edit_text_login_email;
    private TextInputLayout password_toggle_login;
    private Button button_login;
    private ProgressBar progress_bar_login;
    private TextView text_view_sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edit_text_login_email = findViewById(R.id.edit_text_login_email);
        password_toggle_login = findViewById(R.id.password_toggle_login);
        button_login = findViewById(R.id.button_login);
        progress_bar_login = findViewById(R.id.progress_bar_login);
        text_view_sign_up = findViewById(R.id.text_view_sign_up);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        text_view_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpActivity();
            }
        });

        password_toggle_login.getEditText().addTextChangedListener(passwordTextWatcher);
    }

    private TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            password_toggle_login.setHintEnabled(false);

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            password_toggle_login.setHintEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable s) {
            password_toggle_login.setHintEnabled(false);
        }
    };

    private void login(){
        final String email, password;
        boolean error = false;
        String url = "https://rajkumargurung.com.np/futsal/login.php";
        email = edit_text_login_email.getText().toString().trim();
        password = password_toggle_login.getEditText().getText().toString().trim();

        if (email.isEmpty()) {
            edit_text_login_email.setError("Please fill this field");
            error = true;
        }

        if (password.isEmpty()) {
            password_toggle_login.setError("Please fill this field");
            error = true;
        }

        if (!error){
            progress_bar_login.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            final RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progress_bar_login.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    if (response.trim().equals("first_db_error")) {
                        Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_SHORT).show();

                    } else if (response.trim().equals("second_db_error")) {
                        Toast.makeText(getApplicationContext(), "Error while fetching", Toast.LENGTH_SHORT).show();
                    } else if (response.trim().equals("credential_error")) {
                        Toast.makeText(getApplicationContext(), "Invalid email/password", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            int id = jsonResponse.getInt("user_id");
                            String name = jsonResponse.getString("full_name");
                            String fetched_email = jsonResponse.getString("email");
                            String phone = jsonResponse.getString("phone");

                            User user = new User(id, name, fetched_email, phone, password);
                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                            finish();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress_bar_login.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(LoginActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }

    }

    private void openSignUpActivity() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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