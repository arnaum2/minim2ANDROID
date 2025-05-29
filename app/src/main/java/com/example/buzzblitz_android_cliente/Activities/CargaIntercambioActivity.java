// CargaIntercambio.java
package com.example.buzzblitz_android_cliente.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.example.buzzblitz_android_cliente.R;

public class CargaIntercambioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargaintercambio);

        try {
            LottieAnimationView animationCarga = findViewById(R.id.animationView);
            animationCarga.setAnimation(R.raw.cargandomoneda);
            animationCarga.loop(false);
            animationCarga.playAnimation();

            LottieAnimationView animationCarga2 = findViewById(R.id.animation2View);
            animationCarga2.setAnimation(R.raw.cargandocargando);
            animationCarga2.setSpeed(2.5f);
            animationCarga2.loop(true);
            animationCarga2.playAnimation();

            new Handler().postDelayed(() -> {
                setResult(RESULT_OK);
                finish();
            }, 3000);

        } catch (Exception e) {
            Log.e("ErrorLoadingGame", "Error: " + e.getMessage());
            finish();
        }
    }
}