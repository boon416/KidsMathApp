package com.example.individualassignment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LevelSelectionActivity extends AppCompatActivity {

    private String selectedGame = null; // Initially no game selected

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selection);

        Button btnCompare = findViewById(R.id.btnCompare);
        Button btnOrder = findViewById(R.id.btnOrder);
        Button btnCompose = findViewById(R.id.btnCompose);

        Button btnLevel1 = findViewById(R.id.btnLevel1);
        Button btnLevel2 = findViewById(R.id.btnLevel2);
        Button btnLevel3 = findViewById(R.id.btnLevel3);

        // Select game type
        btnCompare.setOnClickListener(v -> {
            selectedGame = "compare";
            Toast.makeText(this, "Selected: Compare", Toast.LENGTH_SHORT).show();
        });

        btnOrder.setOnClickListener(v -> {
            selectedGame = "order";
            Toast.makeText(this, "Selected: Order", Toast.LENGTH_SHORT).show();
        });

        btnCompose.setOnClickListener(v -> {
            selectedGame = "compose";
            Toast.makeText(this, "Selected: Compose", Toast.LENGTH_SHORT).show();
        });

        // Handle level buttons only if a game has been selected
        btnLevel1.setOnClickListener(v -> handleLevelSelected(1));
        btnLevel2.setOnClickListener(v -> handleLevelSelected(2));
        btnLevel3.setOnClickListener(v -> handleLevelSelected(3));
    }

    private void handleLevelSelected(int level) {
        if (selectedGame == null) {
            Toast.makeText(this, "Please select a game first!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent;
        switch (selectedGame) {
            case "order":
                intent = new Intent(this, OrderActivity.class);
                break;
            case "compose":
                intent = new Intent(this, ComposeActivity.class);
                break;
            case "compare":
            default:
                intent = new Intent(this, CompareActivity.class);
                break;
        }

        intent.putExtra("level", level);
        startActivity(intent);
    }
}
