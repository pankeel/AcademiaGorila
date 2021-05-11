package com.academia.gorillas.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;

import com.academia.gorillas.R;
import com.academia.gorillas.util.Utils;
import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnRightOutListener;

import java.util.ArrayList;

public class OnboardingActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            Utils.setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            Utils.setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        fragmentManager = getSupportFragmentManager();

        final PaperOnboardingFragment paperOnboardingFragment = PaperOnboardingFragment.newInstance(getDataForOnboarding());

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, paperOnboardingFragment);
        fragmentTransaction.commit();

        paperOnboardingFragment.setOnRightOutListener(new PaperOnboardingOnRightOutListener() {
            @Override
            public void onRightOut() {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Boolean statusLocked = prefs.edit().putBoolean("first_login", true).commit();

                Intent intent = new Intent(OnboardingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });



    }



    private ArrayList<PaperOnboardingPage> getDataForOnboarding() {

        PaperOnboardingPage src1 = new PaperOnboardingPage("Onboard 1", "Headline", Color.parseColor("#f44336"), R.drawable.logo, R.drawable.ic_baseline_circle_24);
        PaperOnboardingPage src2 = new PaperOnboardingPage("Onboard 2", "Headline", Color.parseColor("#2196f3"), R.drawable.logo, R.drawable.ic_baseline_circle_24);
        PaperOnboardingPage src3 = new PaperOnboardingPage("Onboard 3", "Headline", Color.parseColor("#4caf50"), R.drawable.logo, R.drawable.ic_baseline_circle_24);


        ArrayList<PaperOnboardingPage> elements = new ArrayList<>();
        elements.add(src1);
        elements.add(src2);
        elements.add(src3);

        return elements;
    }
}