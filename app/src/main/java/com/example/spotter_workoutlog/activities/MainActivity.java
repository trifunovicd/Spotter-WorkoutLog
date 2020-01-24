package com.example.spotter_workoutlog.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.spotter_workoutlog.R;
import com.example.spotter_workoutlog.adapters.MainPagerAdapter;
import com.example.spotter_workoutlog.fragments.CategoryFragment;
import com.example.spotter_workoutlog.fragments.ExerciseFragment;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, this);


        viewPager = findViewById(R.id.exercise_view_pager);
        //viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(mainPagerAdapter);

        TabLayout tabs = findViewById(R.id.exercise_tabs);
        tabs.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(CategoryFragment.actionMode != null){
                    CategoryFragment.actionMode.finish();
                }

                if(ExerciseFragment.actionMode != null){
                    ExerciseFragment.actionMode.finish();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && viewPager.getCurrentItem() == 1){
            viewPager.setCurrentItem(0, true);
            return  true;
        }
        else{
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.calendar_menu:
                Intent calendar_intent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivity(calendar_intent);
                return true;
            case R.id.settings_menu:
                Intent settings_intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settings_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
