package com.example.pbl6app.fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public abstract class FragmentBase extends Fragment {
    abstract protected void initView();
    abstract protected void initListener();

    protected final void addFragment(Fragment fragment, int idView) {
        if(getActivity() != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.add(idView, fragment);
            transaction.addToBackStack(fragment.getClass().getName());
            transaction.commit();
        }
    }

    protected final void replaceFragment(Fragment fragment, int idView) {
        if(getActivity() != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(idView, fragment);
            transaction.addToBackStack(fragment.getClass().getName());
            transaction.commit();
        }
    }

    protected final void backToPreviousFrag() {
        if(getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    protected final void clearBackStack() {
        if(getActivity() != null) {
            for(int i = 0; i < getActivity().getSupportFragmentManager().getBackStackEntryCount(); ++i) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
    }
}
