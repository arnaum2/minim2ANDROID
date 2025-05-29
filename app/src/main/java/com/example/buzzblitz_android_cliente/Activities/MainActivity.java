package com.example.buzzblitz_android_cliente.Activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.airbnb.lottie.LottieAnimationView;
import com.example.buzzblitz_android_cliente.R;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

    private boolean isMenuOpen = false;
    private CardView sideMenuCard;
    private LinearLayout menuContent;
    private ImageView ivArrow;
    private LottieAnimationView bees1, bees2, bees3;
    private TextView tvWelcomeMessage;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvWelcomeMessage = findViewById(R.id.tvWelcomeMessage);
        sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("showWelcome", false)) {
            String userId = sharedPreferences.getString("currentUserId", "");
            tvWelcomeMessage.setText("Welcome, " + userId);
            tvWelcomeMessage.setVisibility(View.VISIBLE);

            new Handler().postDelayed(() -> {
                tvWelcomeMessage.setVisibility(View.GONE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("showWelcome", false);
                editor.apply();
            }, 3000);
        }

        TextView tvUserIdCorner = findViewById(R.id.tvUserIdCorner);
        tvUserIdCorner.setText(sharedPreferences.getString("currentUserId", ""));

        bees1 = findViewById(R.id.lottieBees1);
        bees2 = findViewById(R.id.lottieBees2);
        bees3 = findViewById(R.id.lottieBees3);

        new Handler().postDelayed(() -> bees2.setVisibility(View.VISIBLE), 1000);
        new Handler().postDelayed(() -> bees3.setVisibility(View.VISIBLE), 2000);

        findViewById(R.id.btnOptions).setOnClickListener(v ->
                startActivity(new Intent(this, OpcionesActivity.class)));

        findViewById(R.id.btnExit).setOnClickListener(v ->
                startActivity(new Intent(this, CerrarSesionActivity.class)));

        sideMenuCard = findViewById(R.id.sideMenuCard);
        menuContent = findViewById(R.id.menuContent);
        ivArrow = findViewById(R.id.ivArrow);
        setupMenuToggle();
        setupLottieAnimations();
        findViewById(R.id.btnReportIssue).setOnClickListener(v -> {
            // Lanza ReportIssueActivity
            startActivity(new Intent(MainActivity.this, ReportIssueActivity.class));
        });

    }

    private void setupMenuToggle() {
        sideMenuCard.setOnClickListener(v -> toggleMenu());
        ivArrow.setOnClickListener(v -> toggleMenu());
    }

    private void toggleMenu() {
        if (isMenuOpen) {
            menuContent.setVisibility(View.GONE);
            ivArrow.setVisibility(View.VISIBLE);
            ivArrow.setImageResource(R.drawable.ic_arrow_left);
            sideMenuCard.getLayoutParams().width = getResources().getDimensionPixelSize(R.dimen.card_collapsed_width);
        } else {
            menuContent.setVisibility(View.VISIBLE);
            ivArrow.setVisibility(View.GONE);
            sideMenuCard.getLayoutParams().width = getResources().getDimensionPixelSize(R.dimen.card_expanded_width);
        }
        isMenuOpen = !isMenuOpen;
        sideMenuCard.requestLayout();
    }

    private void setupLottieAnimations() {
        int[] animationIds = {R.id.lottieShop, R.id.lottieUser, R.id.lottieSettings, R.id.lottieTrophy, R.id.lottieXat};
        int[] rawFiles = {R.raw.tienda, R.raw.user, R.raw.settings, R.raw.trophy, R.raw.xat};
        Class<?>[] targetActivities = {BeforeTiendaActivity.class, UserProfileActivity.class, SettingsActivity.class, RankingActivity.class, BeforeChatActivity.class};

        for (int i = 0; i < animationIds.length; i++) {
            LottieAnimationView animView = findViewById(animationIds[i]);
            animView.setAnimation(rawFiles[i]);
            final Class<?> activity = targetActivities[i];

            animView.setOnClickListener(v -> {
                animView.playAnimation();
                startActivity(new Intent(MainActivity.this, activity));
            });
        }
    }

    public void jugarClick(View view) {
        try {
            Intent i = new Intent();
            i.setComponent(new ComponentName(
                    "com.DefaultCompany.Buzzblitz",
                    "com.unity3d.player.UnityPlayerGameActivity"
            ));
            startActivityForResult(i, 0);
        } catch (Exception e) {
            Toast.makeText(this, "Instala la app de Unity primero", Toast.LENGTH_SHORT).show();
            Log.e("UnityLaunchError", "Error al lanzar la app Unity", e);
        }
    }
}