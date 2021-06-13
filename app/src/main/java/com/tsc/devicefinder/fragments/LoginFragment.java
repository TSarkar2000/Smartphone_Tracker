package com.tsc.devicefinder.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tsc.devicefinder.R;
import com.tsc.devicefinder.core.Auth;

public class LoginFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_login, container, false);
        EditText email = v.findViewById(R.id.emailTxt);
        EditText pwd = v.findViewById(R.id.pwdText);
        Button btn = v.findViewById(R.id.loginBtn);
        btn.setOnClickListener(v1 -> {
            // request login
            new Auth(email.getText().toString(), pwd.getText().toString()).begin();
        });
        TextView forgotPwd = v.findViewById(R.id.forgotPwd);
        forgotPwd.setOnClickListener(v1 -> {
            // do something
        });
        return v;
    }
}