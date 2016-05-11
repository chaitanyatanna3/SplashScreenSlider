package com.example.chaitanya.splashscreenslider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by chaitanyatanna on 5/11/16.
 */
public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout linearLayoutDots;
    private TextView[] textViewDots;
    private int[] layouts;
    private Button buttonSkip, buttonNext;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //checking for the first time launch - before calling setContentView()
        preferenceManager = new PreferenceManager(this);
        if (!preferenceManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

        //making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }

        setContentView(R.layout.activity_welcome);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        linearLayoutDots = (LinearLayout) findViewById(R.id.layoutDots);
        buttonSkip = (Button) findViewById(R.id.btn_skip);
        buttonNext = (Button) findViewById(R.id.btn_next);

        //layouts of all welcome sliders
        layouts = new int[] {
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3,
                R.layout.welcome_slide4
        };

        //adding bottom dots
        addBottomDots(0);

        //make notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check for the last page, if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    //move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });
    }

    private void addBottomDots(int currentPage) {
        textViewDots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        linearLayoutDots.removeAllViews();

        for (int i = 0; i < textViewDots.length; i ++) {
            textViewDots[i] = new TextView(this);
            textViewDots[i].setText(Html.fromHtml("&#8226;"));
            textViewDots[i].setTextSize(35);
            textViewDots[i].setTextColor(colorsInactive[currentPage]);
            linearLayoutDots.addView(textViewDots[i]);
        }
        if (textViewDots.length > 0) {
            textViewDots[currentPage].setTextColor(colorsActive[currentPage]);
        }
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + 1;
    }

    private void launchHomeScreen() {
        preferenceManager.setFirstTimeLaunch(false);
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
    }

    //view pager listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            //changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                //last page. make button text to GOT IT
                buttonNext.setText(getString(R.string.start));
                buttonSkip.setVisibility(View.GONE);
            } else {
                //still pages left
                buttonNext.setText(getString(R.string.next));
                buttonSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    //making notification bar transparent
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    //view pager adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
