package com.example.managenyama;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_SCREEN = 3000;

    Animation topAnimation, bottomAnimation;
    ImageView appLogo;
    TextView appName, description;
    // ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // progressBar = findViewById(R.id.progress_bar);
        // progressBar.setVisibility(View.VISIBLE);

        // Make pairs to link the already defined animation names

        //Animations
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //hooks
        appLogo = findViewById(R.id.butchery_logo);
        appName = findViewById(R.id.app_name);
        description = findViewById(R.id.app_description);

        //Assigning animations
        appLogo.setAnimation(topAnimation);
        appName.setAnimation(bottomAnimation);
        description.setAnimation(bottomAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, LoginActivity.class);

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(appLogo, "logo_image");
                pairs[1] = new Pair<View, String>(appName, "logo_name");

                // Set Activity Options
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this, pairs);
                    startActivity(i, options.toBundle());
                    finish();
                }
            }
        }, SPLASH_SCREEN);

    }
}