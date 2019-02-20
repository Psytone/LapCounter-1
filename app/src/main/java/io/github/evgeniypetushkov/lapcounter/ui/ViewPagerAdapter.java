package io.github.evgeniypetushkov.lapcounter.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import io.github.evgeniypetushkov.lapcounter.R;

class ViewPagerAdapter extends PagerAdapter {
    private final View swView;
    private final View hView;
    private final Context runsApplication;

    ViewPagerAdapter(View swView, View hView, Context appContext) {
        this.swView = swView;
        this.hView = hView;
        this.runsApplication = appContext;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (position == 0) {
            container.addView(swView);
            return swView;
        } else {
            container.addView(hView);
            return hView;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) return runsApplication.getString(R.string.stopwatch_page_title);
        else return runsApplication.getString(R.string.history_page_title);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
