package com.example.spotter_workoutlog.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotter_workoutlog.R;
import com.google.android.material.textfield.TextInputEditText;

public class Utility {

    public void RepsSubtract(TextInputEditText textInputEditText){
        if(!TextUtils.isEmpty(textInputEditText.getText())){
            int quantity = Integer.parseInt(textInputEditText.getText().toString().trim());
            quantity = quantity - 1;
            if(quantity < 0){
                textInputEditText.setText(String.valueOf(0));
            }
            else{
                textInputEditText.setText(String.valueOf(quantity));
            }
        }
        else{
            textInputEditText.setText(String.valueOf(0));
        }
    }

    public void RepsAdd(TextInputEditText textInputEditText){
        if(!TextUtils.isEmpty(textInputEditText.getText())){
            int quantity = Integer.parseInt(textInputEditText.getText().toString().trim());
            quantity = quantity + 1;
            textInputEditText.setText(String.valueOf(quantity));
        }
        else{
            textInputEditText.setText(String.valueOf(1));
        }
    }

    public void WeightSubtract(TextInputEditText textInputEditText){
        if(!TextUtils.isEmpty(textInputEditText.getText())){
            float quantity = Float.parseFloat(textInputEditText.getText().toString().trim());
            quantity = quantity - 1;
            if(quantity < 0){
                textInputEditText.setText(String.valueOf(0.0));
            }
            else{
                textInputEditText.setText(String.valueOf(quantity));
            }
        }
        else{
            textInputEditText.setText(String.valueOf(0.0));
        }
    }

    public void WeightAdd(TextInputEditText textInputEditText){
        if(!TextUtils.isEmpty(textInputEditText.getText())){
            float quantity = Float.parseFloat(textInputEditText.getText().toString().trim());
            quantity = quantity + 1;
            textInputEditText.setText(String.valueOf(quantity));
        }
        else{
            textInputEditText.setText(String.valueOf(1.0));
        }
    }
}
