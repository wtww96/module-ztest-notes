package com.dp.notes.widget.tablayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * author Dq
 * date on 2021/4/13
 * description  
 */
abstract public class SlidingAdapter extends FragmentStateAdapter {
    public SlidingAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public SlidingAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public SlidingAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    abstract public CharSequence getPageTitle(int position);
}
