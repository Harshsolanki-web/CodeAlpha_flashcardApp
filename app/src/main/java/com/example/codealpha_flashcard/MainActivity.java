package com.example.codealpha_flashcard;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import com.example.codealpha_flashcard.R;


public class MainActivity extends AppCompatActivity {

    TextView flashcardText;
    Button showAnswerBtn, nextBtn, prevBtn, addBtn, editBtn, deleteBtn;

    class Flashcard {
        String question;
        String answer;

        Flashcard(String q, String a) {
            question = q;
            answer = a;
        }
    }

    ArrayList<Flashcard> flashcards = new ArrayList<>();
    int currentIndex = 0;
    boolean showingAnswer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashcardText = findViewById(R.id.flashcardText);
        showAnswerBtn = findViewById(R.id.showAnswerBtn);
        nextBtn = findViewById(R.id.nextBtn);
        prevBtn = findViewById(R.id.prevBtn);
        addBtn = findViewById(R.id.addBtn);
        editBtn = findViewById(R.id.editBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        // Sample data
        flashcards.add(new Flashcard("What is the capital of France?", "Paris"));
        flashcards.add(new Flashcard("What is 2 + 2?", "4"));
        flashcards.add(new Flashcard("What is Android?", "A mobile OS by Google."));

        updateCard();

        showAnswerBtn.setOnClickListener(v -> {
            showingAnswer = !showingAnswer;
            updateCard();
        });

        nextBtn.setOnClickListener(v -> {
            if (currentIndex < flashcards.size() - 1) {
                currentIndex++;
                showingAnswer = false;
                updateCard();
            }
        });

        prevBtn.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                showingAnswer = false;
                updateCard();
            }
        });

        addBtn.setOnClickListener(v -> showAddDialog());

        editBtn.setOnClickListener(v -> showEditDialog());

        deleteBtn.setOnClickListener(v -> {
            if (!flashcards.isEmpty()) {
                flashcards.remove(currentIndex);
                if (currentIndex >= flashcards.size()) currentIndex = flashcards.size() - 1;
                showingAnswer = false;
                updateCard();
            }
        });
    }

    void updateCard() {
        if (flashcards.isEmpty()) {
            flashcardText.setText("No flashcards");
            showAnswerBtn.setEnabled(false);
            return;
        }
        showAnswerBtn.setEnabled(true);
        Flashcard current = flashcards.get(currentIndex);
        flashcardText.setText(showingAnswer ? current.answer : current.question);
    }

    void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Flashcard");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText questionInput = new EditText(this);
        questionInput.setHint("Question");
        layout.addView(questionInput);

        final EditText answerInput = new EditText(this);
        answerInput.setHint("Answer");
        layout.addView(answerInput);

        builder.setView(layout);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String q = questionInput.getText().toString();
            String a = answerInput.getText().toString();
            if (!q.isEmpty() && !a.isEmpty()) {
                flashcards.add(new Flashcard(q, a));
                currentIndex = flashcards.size() - 1;
                showingAnswer = false;
                updateCard();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    void showEditDialog() {
        if (flashcards.isEmpty()) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Flashcard");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText questionInput = new EditText(this);
        questionInput.setHint("Question");
        questionInput.setText(flashcards.get(currentIndex).question);
        layout.addView(questionInput);

        final EditText answerInput = new EditText(this);
        answerInput.setHint("Answer");
        answerInput.setText(flashcards.get(currentIndex).answer);
        layout.addView(answerInput);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String q = questionInput.getText().toString();
            String a = answerInput.getText().toString();
            if (!q.isEmpty() && !a.isEmpty()) {
                flashcards.get(currentIndex).question = q;
                flashcards.get(currentIndex).answer = a;
                showingAnswer = false;
                updateCard();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
