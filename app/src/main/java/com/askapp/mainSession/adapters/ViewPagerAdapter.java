package com.askapp.mainSession.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.askapp.mainSession.mainFragments.AccountFragment;
import com.askapp.mainSession.mainFragments.CallFragment;
import com.askapp.mainSession.mainFragments.ChatFragment;
import com.askapp.mainSession.mainFragments.StatusFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        if (position==0){
            return new ChatFragment();
        }else if (position==1){
            return new StatusFragment();
        }else if (position==2){
            return new CallFragment();
        }else {
            return new AccountFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
