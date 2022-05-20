package com.example.lifetracker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentAdapter extends FragmentStateAdapter {
    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        //Fragment[] fragments = new Fragment[]{new ToDoFragment(),new CalendarFragment(), new BudgetFragment(), new MeFragment()};
        //return fragments[position-1];
        //System.out.println("position="+position);
        switch (position) {
            case 0:
                return new ToDoFragment();
            case 1:
                return new CalendarFragment();
            case 2:
                return new BudgetFragment();
            default:
                return new MeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
