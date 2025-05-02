package com.example.individualassignment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 10);

        TextView txtScore = findViewById(R.id.txtScore);
        Button btnBack = findViewById(R.id.btnBackToMenu);

        txtScore.setText("You got " + score + " out of " + total + " correct!");

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, LevelSelectionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}
