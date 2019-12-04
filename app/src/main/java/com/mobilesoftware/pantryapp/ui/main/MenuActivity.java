package com.mobilesoftware.pantryapp.ui.main;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.tabs.TabLayout;
import com.mobilesoftware.pantryapp.MapActivity;
import com.mobilesoftware.pantryapp.R;
import com.mobilesoftware.pantryapp.ShakeListener;
import com.mobilesoftware.pantryapp.database.FoodRepository;
import com.mobilesoftware.pantryapp.ui.main.adapters.ViewPagerAdapter;


public class MenuActivity extends AppCompatActivity {
    private static final String TAG = "MenuActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    FoodRepository foodRepository;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    ShakeListener shakeListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        foodRepository = new FoodRepository(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        shakeListener = new ShakeListener(this);
        shakeListener.pause();
    }


    public boolean isServiceGood() {
        Log.d(TAG, "isServiceGOOD: checking google service version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MenuActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            Log.d(TAG, "isServiceGOOD: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(TAG, "isServiceGOOD: an error occured");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MenuActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.pantry:
                intent = new Intent(this, PantryActivity.class);
                startActivity(intent);
                break;
            case R.id.recipes:
                intent = new Intent(this, RecipeActivity.class);
                startActivity(intent);
                break;
            case R.id.shopping:
                intent = new Intent(this, ShoppingListActivity.class);
                startActivity(intent);
                break;
            case R.id.store:
                if (isServiceGood()) {
                    intent = new Intent(this, MapActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }
}