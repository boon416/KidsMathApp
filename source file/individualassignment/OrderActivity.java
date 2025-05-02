package com.example.individualassignment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class OrderActivity extends AppCompatActivity {

    private ImageView imgOrder;
    private int level;
    private TextView txtInstruction, txtResult;
    private LinearLayout answerSlots, numberButtons;
    private Button btnSubmit, btnReset;

    private ArrayList<Integer> numbers = new ArrayList<>();
    private ArrayList<Integer> userAnswer = new ArrayList<>();
    private boolean ascendingOrder = true;

    private int score = 0;
    private int currentQuestion = 0;
    private final int totalQuestions = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order);

        level = getIntent().getIntExtra("level", 1);

        txtInstruction = findViewById(R.id.txtInstruction);
        txtResult = findViewById(R.id.txtResult);
        answerSlots = findViewById(R.id.answerSlots);
        numberButtons = findViewById(R.id.numberButtons);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnReset = findViewById(R.id.btnReset);
        imgOrder = findViewById(R.id.imgOrder); // <-- LINK the image!
        Button btnBack = findViewById(R.id.btnBack);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            );
            return insets;
        });

        btnBack.setOnClickListener(v -> finish());
        btnSubmit.setOnClickListener(v -> checkAnswer());
        btnReset.setOnClickListener(v -> {
            userAnswer.clear();
            updateAnswerSlots();
            for (int i = 0; i < numberButtons.getChildCount(); i++) {
                View child = numberButtons.getChildAt(i);
                if (child instanceof Button) {
                    child.setEnabled(true);
                }
            }
            txtResult.setText("");
        });

        generateNewQuestion();
    }

    private void generateNewQuestion() {
        if (currentQuestion >= totalQuestions) {
            goToScorePage();
            return;
        }

        currentQuestion++;

        numbers.clear();
        userAnswer.clear();
        answerSlots.removeAllViews();
        numberButtons.removeAllViews();
        txtResult.setText("");
        imgOrder.setImageResource(R.drawable.order); // Reset image on new question

        ascendingOrder = new Random().nextBoolean();
        txtInstruction.setText(ascendingOrder ? "Show Ascending Order" : "Show Descending Order");

        Random rand = new Random();
        while (numbers.size() < 5) {
            int num;
            if (level == 1) {
                num = rand.nextInt(9) + 1;
            } else if (level == 2) {
                num = rand.nextInt(90) + 10;
            } else {
                num = rand.nextInt(900) + 100;
            }
            if (!numbers.contains(num)) numbers.add(num);
        }

        for (int i = 0; i < 5; i++) {
            TextView slot = new TextView(this);
            slot.setText("___");
            slot.setTextSize(24);
            slot.setPadding(20, 0, 20, 0);
            answerSlots.addView(slot);
        }

        for (int num : numbers) {
            Button btn = new Button(this);
            btn.setText(String.valueOf(num));
            btn.setOnClickListener(v -> {
                if (userAnswer.size() < 5) {
                    userAnswer.add(num);
                    updateAnswerSlots();
                    btn.setEnabled(false);
                }
            });
            numberButtons.addView(btn);
        }
    }

    private void updateAnswerSlots() {
        for (int i = 0; i < answerSlots.getChildCount(); i++) {
            TextView slot = (TextView) answerSlots.getChildAt(i);
            if (i < userAnswer.size()) {
                slot.setText(String.valueOf(userAnswer.get(i)));
            } else {
                slot.setText("___");
            }
        }
    }

    private void checkAnswer() {
        if (userAnswer.size() < 5) {
            txtResult.setText("❗ Please fill all slots.");
            return;
        }

        ArrayList<Integer> correct = new ArrayList<>(numbers);
        if (ascendingOrder) {
            Collections.sort(correct);
        } else {
            correct.sort(Collections.reverseOrder());
        }

        if (userAnswer.equals(correct)) {
            txtResult.setText("✅ Correct!");
            imgOrder.setImageResource(R.drawable.right2); // Success image
            score++;
        } else {
            txtResult.setText("❌ Try Again!");
            imgOrder.setImageResource(R.drawable.wrong2); // Failure image
        }

        new Handler().postDelayed(this::generateNewQuestion, 2000);
    }

    private void goToScorePage() {
        Intent intent = new Intent(this, ScoreActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("total", totalQuestions);
        startActivity(intent);
        finish();
    }
}
