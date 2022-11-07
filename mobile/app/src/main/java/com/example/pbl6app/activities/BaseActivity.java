package com.example.pbl6app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.pbl6app.R;

public class BaseActivity extends AppCompatActivity {

    protected final void addFragment(Fragment fragment, int layoutID, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(layoutID, fragment);
        if(addToBackStack){
            transaction.addToBackStack(fragment.getClass().getName());
        }
        transaction.commit();
    }

    protected final void replaceFragment(Fragment fragment, int layoutID, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(layoutID, fragment);
        if(addToBackStack){
            transaction.addToBackStack(fragment.getClass().getName());
        }
        transaction.commit();
    }

    protected final void clearBackstack() {

        for(int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); ++i) {
            getSupportFragmentManager().popBackStack();
        }
    }
}
