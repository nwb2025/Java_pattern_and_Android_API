package com.example.android.java.utilities;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityHelper {

    // put some logic here
    // then we'll get it within activity classes whenever it's needed
    public static void log(Context context,
                           TextView tv,
                           String message,
                            boolean append){
        if (tv != null && message != null && message.length()> 0 ){
            if (append){
                tv.append(message);
            }else{
                tv.setText(message);
            }
            Log.i(context.getClass().getSimpleName(), message);
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
        }
    }
}
