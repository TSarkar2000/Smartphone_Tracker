package com.tsc.devicefinder.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tsc.devicefinder.fragments.LoginFragment;
import com.tsc.devicefinder.fragments.RegistrationFragment;

public class FragmentAdapter extends FragmentStateAdapter {

    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return position == 0 ? new LoginFragment(): new RegistrationFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
