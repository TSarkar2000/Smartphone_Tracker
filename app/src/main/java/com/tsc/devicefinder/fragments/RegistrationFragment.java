package com.tsc.devicefinder.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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

public class RegistrationFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_registration, container, false);

        EditText name = v.findViewById(R.id.nameTxt2);
        EditText email = v.findViewById(R.id.emailTxt2);
        EditText pwd = v.findViewById(R.id.pwdText2);
        EditText pwd2 = v.findViewById(R.id.pwdTxt3);

        Button btn = v.findViewById(R.id.regBtn);
        btn.setOnClickListener(v1 -> {
            if (!TextUtils.isEmpty(name.getText()))
                if (!TextUtils.isEmpty(email.getText()))
                    if(Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
                        if (!TextUtils.isEmpty(pwd.getText()))
                            if (pwd.getText().toString().length() >= 8)
                                if (!TextUtils.isEmpty(pwd2.getText()))
                                    if (TextUtils.equals(pwd.getText(), pwd2.getText()))
                                        // request registration
                                            new Auth(name.getText().toString(), email.getText().toString(), pwd.getText().toString()).begin();
                                    else
                                        pwd2.setError("Passwords do not match!");
                                else
                                    pwd2.setError("Please confirm your password");
                            else
                                pwd.setError("Password must be atleast 8 characters long!");
                        else
                            pwd.setError("Password can't be empty!");
                     else
                         email.setError("Invalid email");
                else
                    email.setError("Email can't be empty!");
            else
                name.setError("Name can't be empty!");
        });

        TextView existingTxt = v.findViewById(R.id.existingTxt);
        existingTxt.setOnClickListener(v1 -> {
            // do something
        });

        return v;
    }
}