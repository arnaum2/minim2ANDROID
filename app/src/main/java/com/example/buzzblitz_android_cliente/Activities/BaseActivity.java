package com.example.buzzblitz_android_cliente.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.buzzblitz_android_cliente.R;

public class BaseActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private ImageButton ivProfile;
    private ImageButton ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);

        TextView tvUsername = findViewById(R.id.tvUsername);
        tvUsername.setText(getSharedPreferences("MyPreferences", MODE_PRIVATE).getString("currentUserId", ""));

        ivBack = findViewById(R.id.ivBack);
        if (!isCurrentActivity(MainActivity.class)) {
            ivBack.setOnClickListener(v -> onBackPressed());
        } else {
            ivBack.setEnabled(false);
            ivBack.setClickable(false);
        }

        ivProfile = findViewById(R.id.ivProfile);
        ivProfile.setOnClickListener(v -> {
            if (!isCurrentActivity(UserProfileActivity.class)) {
                startActivity(new Intent(this, UserProfileActivity.class));
            }
        });

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_shop) {
                navigateToActivity(BeforeTiendaActivity.class);
            } else if (itemId == R.id.nav_home) {
                navigateToActivity(MainActivity.class);
            } else if (itemId == R.id.nav_trophy) {
                navigateToActivity(RankingActivity.class);
            }
            updateBottomNavState(itemId);
            return true;
        });
    }

    private void navigateToActivity(Class<?> activity) {
        if (!this.getClass().equals(activity)) {
            startActivity(new Intent(this, activity));
            finish();
        }
    }

    private boolean isCurrentActivity(Class<?> activityClass) {
        return this.getClass().equals(activityClass);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isCurrentActivity(MainActivity.class)) {
            ivBack.setEnabled(false);
            ivBack.setClickable(false);
        } else {
            ivBack.setEnabled(true);
            ivBack.setClickable(true);
        }
        ivProfile.setEnabled(!isCurrentActivity(UserProfileActivity.class));
        updateBottomNavState(getCurrentNavId());
    }

    @Override
    public void setContentView(int layoutResID) {
        FrameLayout contentFrame = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(layoutResID, contentFrame, true);
        updateBottomNavState(getCurrentNavId());
    }

    private void updateBottomNavState(int selectedItemId) {
        for (int i = 0; i < bottomNav.getMenu().size(); i++) {
            MenuItem item = bottomNav.getMenu().getItem(i);
            item.setChecked(false);
            item.setEnabled(true);
        }

        MenuItem selectedItem = bottomNav.getMenu().findItem(selectedItemId);
        if (selectedItem != null) {
            selectedItem.setChecked(true);
            selectedItem.setEnabled(false);
        }
    }

    private int getCurrentNavId() {
        if (this instanceof MainActivity) return R.id.nav_home;
        else if (this instanceof BeforeTiendaActivity) return R.id.nav_shop;
        else if (this instanceof RankingActivity) return R.id.nav_trophy;
        return -1;
    }
}