package com.example.pbl6app.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.pbl6app.fragment.InProgressOrderFragment;
import com.example.pbl6app.fragment.WaitingOrderFragment;

public class StatusFragmentAdapter extends FragmentStateAdapter {
    public StatusFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public StatusFragmentAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public StatusFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new InProgressOrderFragment();
            case 1:
                return new WaitingOrderFragment();
            default:
                return new InProgressOrderFragment();

        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }


}
