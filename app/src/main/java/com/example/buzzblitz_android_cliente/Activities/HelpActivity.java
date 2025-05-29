package com.example.buzzblitz_android_cliente.Activities;

import android.animation.Animator;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.buzzblitz_android_cliente.R;

public class HelpActivity extends BaseActivity {
    private int clickCount = 0;
    private long lastClickTime = 0;
    private static final int CLICK_THRESHOLD = 3;
    private static final long CLICK_TIMEOUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        TextView tvUserIdCorner = findViewById(R.id.tvUserIdCorner);
        tvUserIdCorner.setText(sharedPreferences.getString("currentUserId", ""));

        final LottieAnimationView explosionAnimation = findViewById(R.id.animationView);
        ImageView honeyJarImage = findViewById(R.id.imageView6);

        honeyJarImage.setOnClickListener(v -> handleHoneyJarClick(explosionAnimation));

        explosionAnimation.setOnClickListener(v -> {
            explosionAnimation.cancelAnimation();
            explosionAnimation.setVisibility(View.INVISIBLE);
        });
    }

    private void handleHoneyJarClick(LottieAnimationView animation) {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastClickTime < CLICK_TIMEOUT) {
            clickCount++;
            if (clickCount >= CLICK_THRESHOLD) {
                triggerAnimation(animation);
                clickCount = 0;
            }
        } else {
            clickCount = 1;
        }
        lastClickTime = currentTime;
    }

    private void triggerAnimation(LottieAnimationView animation) {
        animation.setVisibility(View.VISIBLE);
        animation.playAnimation();
        animation.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animator) {
                animation.postDelayed(() -> {
                    if (animation.getVisibility() == View.VISIBLE) {
                        animation.setVisibility(View.INVISIBLE);
                    }
                }, 500);
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
    }
}