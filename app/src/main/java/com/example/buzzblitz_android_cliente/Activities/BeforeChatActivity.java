package com.example.buzzblitz_android_cliente.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.buzzblitz_android_cliente.R;

public class BeforeChatActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beforechat);

        ImageButton btnChat = findViewById(R.id.boton_imagenchat);
        ImageButton btnForum = findViewById(R.id.boton_imagenforum);

        Button btnAmigos = findViewById(R.id.btnAmigos);
        btnAmigos.setOnClickListener(v ->
                startActivity(new Intent(this, FriendsListActivity.class))
        );
        btnChat.setOnClickListener(v ->
                startActivity(new Intent(this, ChatListActivity.class))
        );

        btnForum.setOnClickListener(v ->
                startActivity(new Intent(this, ForumTopicListActivity.class))
        );
    }
}