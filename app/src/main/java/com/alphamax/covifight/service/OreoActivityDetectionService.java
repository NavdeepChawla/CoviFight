package com.alphamax.covifight.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.LocationManager;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class OreoActivityDetectionService extends IntentService {
    protected static final String TAG = "Activity";
    //Call the super IntentService constructor with the name for the worker thread//

    //Temp variables
    private LocationManager lm;
    private FirebaseFirestore db;
    private FusedLocationProviderClient fusedLocationClient;
    public OreoActivityDetectionService() {
        super(TAG);
    }

    public static Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();
        gson=new Gson();
    }
//Define an onHandleIntent() method, which will be called whenever an activity detection update is available//

    @Override
    protected void onHandleIntent(Intent intent) {

        //Check whether the Intent contains activity recognition data//
        if (ActivityRecognitionResult.hasResult(intent)) {

            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

            DetectedActivity mostProbableActivity = result.getMostProbableActivity();
            int activityType = mostProbableActivity.getType();
            String Activity=getActivityString(this,activityType);

            //PutActivity in sharedPreference
            SharedPreferences.Editor editor = getSharedPreferences(getPackageName(), MODE_PRIVATE).edit();
            editor.putString("Activity",Activity);
            editor.apply();
        }
    }
//Convert the code for the detected activity type, into the corresponding string//

    static String getActivityString(Context context, int detectedActivityType) {
        Resources resources = context.getResources();
        switch(detectedActivityType) {
            case DetectedActivity.ON_BICYCLE:
                return  "BICYCLE";
            case DetectedActivity.ON_FOOT:
                return "FOOT";
            case DetectedActivity.RUNNING:
                return "RUNNING";
            case DetectedActivity.STILL:
                return "STILL";
            case DetectedActivity.TILTING:
                return "TILTING";
            case DetectedActivity.WALKING:
                return "WALKING";
            case DetectedActivity.IN_VEHICLE:
                return "VEHICLE";
            default:
                return "UNKNOWN";
        }
    }

}
