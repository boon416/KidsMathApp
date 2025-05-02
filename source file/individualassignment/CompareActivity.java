package com.example.individualassignment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.ImageView;

import java.util.Random;

public class CompareActivity extends AppCompatActivity {

    private ImageView imgFeedback;
    private int level;
    private boolean askBigger; // true = bigger, false = smaller
    private TextView txtPrompt, txtNumbers, txtResult;
    private Button btnLeft, btnRight;

    private int leftNumber, rightNumber;

    // Score system
    private int score = 0;
    private int currentQuestion = 0;
    private final int totalQuestions = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_compare);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imgFeedback = findViewById(R.id.imgFeedback);

        level = getIntent().getIntExtra("level", 1); // Default to Level 1

        txtPrompt = findViewById(R.id.txtPrompt);
        txtNumbers = findViewById(R.id.txtNumbers);
        txtResult = findViewById(R.id.txtResult);
        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);
        Button btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        generateNumbers();

        btnLeft.setOnClickListener(v -> handleAnswer(leftNumber, rightNumber));
        btnRight.setOnClickListener(v -> handleAnswer(rightNumber, leftNumber));
    }

    private void handleAnswer(int chosen, int other) {
        boolean correct = (askBigger && chosen > other) || (!askBigger && chosen < other);

        if (correct) {
            txtResult.setText("✅ Correct!");
            imgFeedback.setImageResource(R.drawable.right1); // 👈 Correct image
            score++;
        } else {
            txtResult.setText("❌ Try Again!");
            imgFeedback.setImageResource(R.drawable.wrong1); // 👈 Wrong image
        }

        currentQuestion++;

        new Handler().postDelayed(() -> {
            txtResult.setText("");
            if (currentQuestion >= totalQuestions) {
                goToScorePage();
            } else {
                generateNumbers();
            }
        }, 2000);
    }

    private void generateNumbers() {
        Random rand = new Random();

        // Choose "Which is Bigger" or "Which is Smaller"
        askBigger = rand.nextBoolean();
        txtPrompt.setText(askBigger ? "Which is Bigger?" : "Which is Smaller?");

        // Generate non-equal numbers based on level
        do {
            if (level == 1) {
                leftNumber = rand.nextInt(9) + 1;
                rightNumber = rand.nextInt(9) + 1;
            } else if (level == 2) {
                leftNumber = rand.nextInt(90) + 10;
                rightNumber = rand.nextInt(90) + 10;
            } else {
                leftNumber = rand.nextInt(900) + 100;
                rightNumber = rand.nextInt(900) + 100;
            }
        } while (leftNumber == rightNumber);

        txtNumbers.setText(leftNumber + "   vs   " + rightNumber);
        imgFeedback.setImageResource(R.drawable.compare); // 👈 Reset image

    }

    private void goToScorePage() {
        Intent intent = new Intent(this, ScoreActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("total", totalQuestions);
        startActivity(intent);
        finish();
    }
}
