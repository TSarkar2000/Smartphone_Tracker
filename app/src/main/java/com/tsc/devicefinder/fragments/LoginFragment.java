package com.tsc.devicefinder.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tsc.devicefinder.AuthActivity;
import com.tsc.devicefinder.R;
import com.tsc.devicefinder.core.Auth;

public class LoginFragment extends Fragment {

    AuthActivity x;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_login, container, false);

        SharedPreferences preferences = getContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
        x = (AuthActivity)getActivity();
        x.preferences = preferences;

        EditText email = v.findViewById(R.id.emailTxt);
        EditText pwd = v.findViewById(R.id.pwdText);
        Button btn = v.findViewById(R.id.loginBtn);

        if(preferences.contains("email")) {
            email.setText(preferences.getString("email", ""));
            pwd.setText(preferences.getString("password", ""));
        }

        btn.setOnClickListener(v1 -> {
            String mail = email.getText().toString();
            String password = pwd.getText().toString();

            if(Patterns.EMAIL_ADDRESS.matcher(mail).matches())
                if(password.length() >= 8) {
                    x.email = mail;
                    x.password = password;
                    // request login
                    new Auth(mail, password).begin(getParentFragmentManager());
                }
                else
                    pwd.setError("Please check your password!");
            else
                email.setError("Invalid email id");

        });
        /*TextView forgotPwd = v.findViewById(R.id.forgotPwd);
        forgotPwd.setOnClickListener(v1 -> {
            // do something
        });*/
        return v;
    }
}