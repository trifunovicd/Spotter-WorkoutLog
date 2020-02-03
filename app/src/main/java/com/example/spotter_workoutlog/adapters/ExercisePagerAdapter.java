package com.example.spotter_workoutlog.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.spotter_workoutlog.R;
import com.example.spotter_workoutlog.fragments.GraphExerciseFragment;
import com.example.spotter_workoutlog.fragments.HistoryExerciseFragment;
import com.example.spotter_workoutlog.fragments.NewExerciseFragment;


public class ExercisePagerAdapter extends FragmentPagerAdapter {

    private int exercise_id;
    private String exercise_name;

    public void setArgs(int exercise_id, String exercise_name) {
        this.exercise_id = exercise_id;
        this.exercise_name = exercise_name;
    }

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;

    public ExercisePagerAdapter(@NonNull FragmentManager fm, int behavior, Context mContext) {
        super(fm, behavior);
        this.mContext = mContext;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return NewExerciseFragment.newInstance(exercise_id);

            case 1:
                return HistoryExerciseFragment.newInstance(exercise_id, exercise_name);

            case 2:
                return GraphExerciseFragment.newInstance(exercise_id);

            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {

        return 3;
    }
}