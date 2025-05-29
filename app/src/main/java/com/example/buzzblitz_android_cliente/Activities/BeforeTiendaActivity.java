//package com.example.buzzblitz_android_cliente.Activities;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.buzzblitz_android_cliente.R;
//
//public class BeforeTiendaActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_beforetienda);
//
//        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
//        TextView tvUserIdCorner = findViewById(R.id.tvUserIdCorner);
//        tvUserIdCorner.setText(sharedPreferences.getString("currentUserId", ""));
//
//        findViewById(R.id.btnBack).setOnClickListener(v ->
//                startActivity(new Intent(this, MainActivity.class)));
//
//        findViewById(R.id.btnComprarTienda).setOnClickListener(v ->
//                startActivity(new Intent(this, TiendaActivity.class)));
//
//        findViewById(R.id.btnIntercambiarTienda).setOnClickListener(v ->
//                startActivity(new Intent(this, IntercambioActivity.class)));
//    }
//}

package com.example.buzzblitz_android_cliente.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.buzzblitz_android_cliente.R;

public class BeforeTiendaActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beforetienda);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        TextView tvUserIdCorner = findViewById(R.id.tvUserIdCorner);
        tvUserIdCorner.setText(sharedPreferences.getString("currentUserId", ""));

        findViewById(R.id.boton_imagenbuy).setOnClickListener(v ->
                startActivity(new Intent(this, TiendaActivity.class))
        );

        findViewById(R.id.boton_imagenexchange).setOnClickListener(v ->
                startActivity(new Intent(this, IntercambioActivity.class))
        );
    }
}