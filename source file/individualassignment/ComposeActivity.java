package com.example.individualassignment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class ComposeActivity extends AppCompatActivity {

    private ImageView imgFeedback;
    private int level;
    private int targetNumber;
    private int option1, option2, option3, option4;
    private Integer selected1 = null, selected2 = null;

    private TextView txtPrompt, txtResult;
    private GridLayout gridOptions;
    private Button btnSubmit, btnReset;
    private Button[] optionButtons = new Button[4];

    private int score = 0;
    private int currentQuestion = 0;
    private final int totalQuestions = 10;
    private boolean answered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        level = getIntent().getIntExtra("level", 1);

        txtPrompt = findViewById(R.id.txtPrompt);
        txtResult = findViewById(R.id.txtResult);
        gridOptions = findViewById(R.id.gridOptions);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnReset = findViewById(R.id.btnReset);
        Button btnBack = findViewById(R.id.btnBack);

        imgFeedback = findViewById(R.id.imgFeedback);

        btnBack.setOnClickListener(v -> finish());
        btnSubmit.setOnClickListener(v -> handleSubmit());
        btnReset.setOnClickListener(v -> resetSelection());

        generateNewQuestion();
    }

    private void generateNewQuestion() {
        if (currentQuestion >= totalQuestions) {
            goToScorePage();
            return;
        }

        currentQuestion++;
        selected1 = null;
        selected2 = null;
        answered = false;
        txtResult.setText("");
        gridOptions.removeAllViews();

        Random rand = new Random();
        if (level == 1) {
            targetNumber = rand.nextInt(8) + 2;
        } else if (level == 2) {
            targetNumber = rand.nextInt(40) + 11;
        } else {
            targetNumber = rand.nextInt(50) + 51;
        }
        imgFeedback.setImageResource(R.drawable.compose);  // ← default image

        txtPrompt.setText("🧩 Make This Number: " + targetNumber);

        int part1 = rand.nextInt(targetNumber - 1) + 1;
        int part2 = targetNumber - part1;

        // Generate unique fake values
        do {
            option1 = part1;
            option2 = part2;
            option3 = rand.nextInt(targetNumber - 1) + 1;
        } while (option3 == option1 || option3 == option2);

        do {
            option4 = rand.nextInt(targetNumber - 1) + 1;
        } while (option4 == option1 || option4 == option2 || option4 == option3);

        int[] values = {option1, option2, option3, option4};
        shuffle(values);

        // Create and display buttons
        for (int i = 0; i < 4; i++) {
            int value = values[i];
            Button btn = new Button(this);
            btn.setText(String.valueOf(value));
            btn.setTextSize(28f);
            btn.setWidth(300);
            btn.setHeight(160);
            btn.setAllCaps(false);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setMargins(16, 16, 16, 16);
            btn.setLayoutParams(params);

            int finalI = i;
            btn.setOnClickListener(v -> handleSelection(btn, value));
            optionButtons[i] = btn;
            gridOptions.addView(btn);
        }
    }

    private void handleSelection(Button btn, int num) {
        if (answered) return;

        if (selected1 == null) {
            selected1 = num;
            btn.setEnabled(false);
        } else if (selected2 == null && num != selected1) {
            selected2 = num;
            btn.setEnabled(false);
        }
    }

    private void resetSelection() {
        selected1 = null;
        selected2 = null;
        txtResult.setText("");
        for (Button btn : optionButtons) {
            btn.setEnabled(true);
        }
    }

    private void handleSubmit() {
        if (answered || selected1 == null || selected2 == null) {
            txtResult.setText("❗ Pick 2 numbers");
            return;
        }

        int sum = selected1 + selected2;
        if (sum == targetNumber) {
            txtResult.setText("✅ Correct!");
            imgFeedback.setImageResource(R.drawable.right3);  // ← image for correct
            score++;
        } else {
            txtResult.setText("❌ Try Again!");
            imgFeedback.setImageResource(R.drawable.wrong3);  // ← image for wrong
        }

        answered = true;
        new Handler().postDelayed(() -> {
            txtResult.setText("");
            generateNewQuestion();
        }, 1500);
    }

    private void goToScorePage() {
        Intent intent = new Intent(this, ScoreActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("total", totalQuestions);
        startActivity(intent);
        finish();
    }

    private void shuffle(int[] array) {
        Random rand = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }
}
