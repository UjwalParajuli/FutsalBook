package com.example.futsalbook.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.futsalbook.BookingHistory;
import com.example.futsalbook.ChangePasswordActivity;
import com.example.futsalbook.EditProfileActivity;
import com.example.futsalbook.LoginActivity;
import com.example.futsalbook.NotificationActivity;
import com.example.futsalbook.R;
import com.example.futsalbook.models.User;
import com.example.futsalbook.utils.SharedPrefManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    private TextView text_view_profile_name, text_view_edit_profile, text_view_edit_password, text_view_logout, text_view_notification, text_view_booking_history;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        getActivity().setTitle("Profile");

        user = SharedPrefManager.getInstance(getContext()).getUser();

        text_view_profile_name = view.findViewById(R.id.text_view_profile_name);
        text_view_edit_profile = view.findViewById(R.id.text_view_edit_profile);
        text_view_edit_password = view.findViewById(R.id.text_view_edit_password);
        text_view_logout = view.findViewById(R.id.text_view_logout);
        text_view_notification = view.findViewById(R.id.text_view_notification);
        text_view_booking_history = view.findViewById(R.id.text_view_booking_history);

        text_view_profile_name.setText(user.getFull_name());

        text_view_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });

        text_view_edit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPassword();
            }
        });

        text_view_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        text_view_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNotifications();
            }
        });

        text_view_booking_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBookingHistory();
            }
        });

        return view;
    }

    private void editProfile(){
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }

    private void openNotifications(){
        Intent intent = new Intent(getContext(), NotificationActivity.class);
        startActivity(intent);
    }

    private void openBookingHistory(){
        Intent intent = new Intent(getContext(), BookingHistory.class);
        startActivity(intent);
    }

    private void editPassword(){
        Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
        startActivity(intent);
    }

    private void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Logout?");
        builder.setMessage("Are you sure want to logout?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().finish();
                SharedPrefManager.getInstance(getContext()).logout();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);

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
