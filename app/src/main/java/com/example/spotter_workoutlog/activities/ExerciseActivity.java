package com.example.spotter_workoutlog.activities;

import android.os.Bundle;

import com.example.spotter_workoutlog.R;
import com.example.spotter_workoutlog.fragments.GraphExerciseFragment;
import com.example.spotter_workoutlog.fragments.HistoryExerciseFragment;
import com.example.spotter_workoutlog.fragments.NewExerciseFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spotter_workoutlog.adapters.ExercisePagerAdapter;

public class ExerciseActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    private String exercise_name;
    private int exercise_id;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            exercise_name = extras.getString("exercise_name");
            exercise_id = extras.getInt("exercise_id");
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(exercise_name);
        setSupportActionBar(toolbar);

        ExercisePagerAdapter exercisePagerAdapter = new ExercisePagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, this);
        exercisePagerAdapter.setArgs(exercise_id, exercise_name);

        viewPager = findViewById(R.id.exercise_view_pager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(exercisePagerAdapter);

        TabLayout tabs = findViewById(R.id.exercise_tabs);
        tabs.setupWithViewPager(viewPager);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(HistoryExerciseFragment.actionMode != null){
                    HistoryExerciseFragment.actionMode.finish();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && viewPager.getCurrentItem() == 0){
            String tag = "android:switcher:" + R.id.exercise_view_pager + ":" + 0;
            NewExerciseFragment fragment = (NewExerciseFragment) getSupportFragmentManager().findFragmentByTag(tag);
            fragment.myOnKeyDown(keyCode);
            return true;
        }
        else if(keyCode == KeyEvent.KEYCODE_BACK && (viewPager.getCurrentItem() == 1 || viewPager.getCurrentItem() == 2)){
            viewPager.setCurrentItem(0, true);
            return  true;
        }
        else{
            return super.onKeyDown(keyCode, event);
        }
    }


}